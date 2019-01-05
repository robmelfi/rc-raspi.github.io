package com.robmelfi.rcraspi.service.scheduled;

import com.pi4j.io.gpio.Pin;
import com.robmelfi.rcraspi.service.FlameSensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledFuture;

@Service
public class ScheduleFlameSensorController {

    private final Logger log = LoggerFactory.getLogger(ScheduleFlameSensorController.class);

    private final FlameSensorService flameSensorService;

    private final TaskScheduler taskScheduler;

    private ScheduledFuture scheduledFuture;

    public ScheduleFlameSensorController(FlameSensorService flameSensorService, TaskScheduler taskScheduler) {
        this.flameSensorService = flameSensorService;
        this.taskScheduler = taskScheduler;
    }

    public void setPin(Pin pin) {
        this.flameSensorService.setPin(pin);
    }

    public void start(long rate) {
        scheduledFuture = taskScheduler.scheduleAtFixedRate(flameDetected(), rate);
    }

    public void stop() {
        scheduledFuture.cancel(false);
    }

    private Runnable flameDetected() {
        return this::task;
    }

    private void task() {
        boolean flame = this.flameSensorService.flameDetected();
        // TODO TRIGGER ACTION
        log.info("Flame Detected: {}", flame);
    }
}
