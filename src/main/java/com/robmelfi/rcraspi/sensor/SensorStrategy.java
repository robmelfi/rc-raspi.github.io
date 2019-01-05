package com.robmelfi.rcraspi.sensor;

import com.pi4j.io.gpio.Pin;

public interface SensorStrategy {

    void enable();

    void disable();

    void setPin(Pin pin);
}
