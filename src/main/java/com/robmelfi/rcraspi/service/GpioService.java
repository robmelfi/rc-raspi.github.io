package com.robmelfi.rcraspi.service;

import com.robmelfi.rcraspi.domain.Controller;

public interface GpioService {

    void setHigh(String pinName);

    void setLow(String pinName);

    void toggle(String pinName);

    boolean getState(String pinName);

    void addController(Controller c, boolean update, String oldPinName);

    void removeController(Long id);
}
