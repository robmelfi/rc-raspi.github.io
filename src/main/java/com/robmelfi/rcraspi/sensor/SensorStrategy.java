package com.robmelfi.rcraspi.sensor;

public interface SensorStrategy {

    void enable();

    void disable();

    void setPin(int name);
}
