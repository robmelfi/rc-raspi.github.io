package com.robmelfi.rcraspi.service;

import com.pi4j.io.gpio.Pin;

public interface FlameSensorService {
    boolean flameDetected();

    void setPin(Pin pin);
}
