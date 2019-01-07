package com.robmelfi.rcraspi.sensor.impl;

import com.pi4j.io.gpio.Pin;
import com.robmelfi.rcraspi.sensor.SensorStrategy;
import com.robmelfi.rcraspi.service.scheduled.ScheduleFlameSensorController;
import org.springframework.stereotype.Component;

@Component
public class FlameSensorManager implements SensorStrategy {

    private final ScheduleFlameSensorController scheduleFlameSensorController;

    private final int DELAY = 1000 * 60; // every minute

    public FlameSensorManager(ScheduleFlameSensorController scheduleFlameSensorController) {
        this.scheduleFlameSensorController = scheduleFlameSensorController;
    }

    @Override
    public void setPin(Pin pin) {
        this.scheduleFlameSensorController.setPin(pin);
    }

    @Override
    public void enable() {
        this.scheduleFlameSensorController.start(DELAY);
    }

    @Override
    public void disable() {
        this.scheduleFlameSensorController.stop();
    }
}
