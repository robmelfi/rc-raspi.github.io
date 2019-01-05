package com.robmelfi.rcraspi.sensor.impl;

import com.pi4j.io.gpio.Pin;
import com.robmelfi.rcraspi.sensor.SensorStrategy;
import com.robmelfi.rcraspi.service.scheduled.ScheduleDHT11Controller;
import org.springframework.stereotype.Component;

@Component
public class DHT11Manager implements SensorStrategy {

    private final ScheduleDHT11Controller scheduleDHT11Controller;

    private int pin;

    private final int DELAY = 1000 * 60; // every minute

    public DHT11Manager(ScheduleDHT11Controller scheduleDHT11Controller) {
        this.scheduleDHT11Controller = scheduleDHT11Controller;
    }

    @Override
    public void setPin(Pin pin) {
        this.pin = pin.getAddress();
    }

    @Override
    public void enable() {
        scheduleDHT11Controller.start(this.pin, DELAY);
    }

    @Override
    public void disable() {
        scheduleDHT11Controller.stop();
    }
}
