package com.robmelfi.rcraspi.service;

public interface RemoteControllerService {

    void on(String pinName);

    void off(String pinName);

    void toggle(String pinName);
}
