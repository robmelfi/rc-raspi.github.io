package com.robmelfi.rcraspi.timer;

import com.robmelfi.rcraspi.domain.Controller;
import com.robmelfi.rcraspi.domain.Pin;
import com.robmelfi.rcraspi.domain.Timer;
import com.robmelfi.rcraspi.domain.enumeration.Repeat;
import com.robmelfi.rcraspi.repository.ControllerRepository;
import com.robmelfi.rcraspi.repository.PinRepository;
import com.robmelfi.rcraspi.repository.TimerRepository;
import com.robmelfi.rcraspi.service.RemoteControllerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Component
public class TimerManager {

    private final Logger log = LoggerFactory.getLogger(TimerManager.class);

    private final ControllerRepository controllerRepository;

    private final TimerRepository timerRepository;

    private final PinRepository pinRepository;

    private final TaskScheduler taskScheduler;

    private final Map<String, ScheduledFuture> scheduledFutureMap;

    private final RemoteControllerService remoteControllerService;

    public TimerManager(ControllerRepository controllerRepository, TimerRepository timerRepository, PinRepository pinRepository, TaskScheduler taskScheduler, RemoteControllerService remoteControllerService) {
        this.controllerRepository = controllerRepository;
        this.timerRepository = timerRepository;
        this.pinRepository = pinRepository;
        this.taskScheduler = taskScheduler;
        this.remoteControllerService = remoteControllerService;

        this.scheduledFutureMap = new HashMap<>();
        this.getTimer();
    }

    public void updateTimer(Timer timer) {
        List<Controller> controllers = this.controllerRepository.findByTimerId(timer.getId());
        for (Controller c: controllers) {
            removeTimer(c.getId());
            addTimer(timer, c);
        }
    }

    public void addTimer(Controller controller) {
        if (controller.getTimer() != null) {
            Timer timer = this.timerRepository.findById(controller.getTimer().getId()).get();
            if (controller.getPin().getName() == null) {
                Pin pin = this.pinRepository.findById(controller.getPin().getId()).get();
                controller.getPin().setName(pin.getName());
            }
            this.addTimer(timer, controller);
        }
    }

    public void removeTimer(Long id) {
        Controller controller = this.controllerRepository.findById(id).get();
        if (controller.getTimer() != null) {
            Timer timer = this.timerRepository.findById(controller.getTimer().getId()).get();
            this.removeTimer(timer, controller);
        }

    }

    private void addTimer(Timer timer, Controller controller) {
        ScheduledFuture scheduledFutureStart;
        ScheduledFuture scheduledFutureStop;
        Repeat repeat = timer.getRepeat();
        if (repeat.equals(Repeat.ONCE)) {
            scheduledFutureStart = this.taskScheduler.schedule(this.start(controller.getPin().getName()), timer.getStart().toInstant());
            scheduledFutureStop = this.taskScheduler.schedule(this.stop(controller.getPin().getName()), timer.getStop().toInstant());
        } else {
            String cronExpressionStart = getCronExpression(timer.getStart(), timer.getRepeat());
            String cronExpressionStop = getCronExpression(timer.getStop(), timer.getRepeat());
            log.debug("START -> {} - {}", timer.getStart(), cronExpressionStart);
            log.debug("STOP  -> {} - {}", timer.getStop(), cronExpressionStop);
            scheduledFutureStart = this.taskScheduler.schedule(this.start(controller.getPin().getName()), new CronTrigger(cronExpressionStart));
            scheduledFutureStop = this.taskScheduler.schedule(this.stop(controller.getPin().getName()), new CronTrigger(cronExpressionStop));
        }
        this.scheduledFutureMap.put(getStartKey(controller.getId().toString(), timer.getId().toString()), scheduledFutureStart);
        this.scheduledFutureMap.put(getStopKey(controller.getId().toString(), timer.getId().toString()), scheduledFutureStop);
    }

    private String getCronExpression(ZonedDateTime datetime, Repeat repeat) {
        LocalDateTime localDateTime = datetime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        int hour = localDateTime.getHour();
        int minute = localDateTime.getMinute();
        int dayOfWeek = localDateTime.getDayOfWeek().getValue();
        int dayOfMonth = localDateTime.getDayOfMonth();
        int month = localDateTime.getMonthValue();
        int year = localDateTime.getYear();
        switch (repeat) {
            case DAY:
                return "0 " + minute + " " + hour + " ? * *";
            case WEEK:
                return "0 " + minute + " " + hour + " ? * " + dayOfWeek;
            case MONTH:
                return "0 " + minute + " " + hour + " " + dayOfMonth + " * ?";
            case YEAR:
                return "0 " + minute + " " + hour + " " + dayOfMonth + " " + month + " ?";
            default:
                return "0 " + minute + " " + hour + " " + dayOfMonth + " " + month + " ?";
        }
    }

    private void removeTimer(Timer timer, Controller controller) {
        String startKey = getStartKey(controller.getId().toString(), timer.getId().toString());
        String stopKey = getStopKey(controller.getId().toString(), timer.getId().toString());
        ScheduledFuture scheduledFutureStart = this.scheduledFutureMap.remove(startKey);
        scheduledFutureStart.cancel(true);
        ScheduledFuture scheduledFutureStop = this.scheduledFutureMap.remove(stopKey);
        scheduledFutureStop.cancel(true);
    }

    private void getTimer() {
        List<Controller> controllerList = this.controllerRepository.findAll();
        for (Controller controller: controllerList) {
            if (controller.getTimer() != null) {
                addTimer(controller.getTimer(), controller);
            }
        }
    }

    private String getStartKey(String controllerID, String timerID) {
        return "START_" + controllerID + "_" + timerID;
    }

    private String getStopKey(String controllerID, String timerID) {
        return "STOP" + controllerID + "_" + timerID;
    }

    private Runnable stop(String pin) {
        return () -> this.remoteControllerService.setHigh(pin);
    }

    private Runnable start(String pin) {
        return () -> this.remoteControllerService.setLow(pin);
    }
}
