package com.robmelfi.rcraspi.timer;

import com.robmelfi.rcraspi.domain.Controller;
import com.robmelfi.rcraspi.domain.Pin;
import com.robmelfi.rcraspi.domain.Timer;
import com.robmelfi.rcraspi.repository.ControllerRepository;
import com.robmelfi.rcraspi.repository.PinRepository;
import com.robmelfi.rcraspi.repository.TimerRepository;
import com.robmelfi.rcraspi.service.RemoteControllerService;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Component
public class TimerManager {

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
        ScheduledFuture scheduledFutureStart = this.taskScheduler.schedule(this.start(controller.getPin().getName()), timer.getStart().toInstant());
        this.scheduledFutureMap.put(getStartKey(controller.getId().toString(), timer.getId().toString()), scheduledFutureStart);
        ScheduledFuture scheduledFutureStop = this.taskScheduler.schedule(this.stop(controller.getPin().getName()), timer.getStop().toInstant());
        this.scheduledFutureMap.put(getStopKey(controller.getId().toString(), timer.getId().toString()), scheduledFutureStop);
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

    private Runnable start(String pin) {
        return () -> this.remoteControllerService.setHigh(pin);
    }

    private Runnable stop(String pin) {
        return () -> this.remoteControllerService.setLow(pin);
    }
}
