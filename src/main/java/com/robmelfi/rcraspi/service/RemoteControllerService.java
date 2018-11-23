package com.robmelfi.rcraspi.service;

public interface RemoteControllerService {

    void setHigh(String pinName);

    void setLow(String pinName);

    void toggle(String pinName);

    boolean getState(String pinName);
}
