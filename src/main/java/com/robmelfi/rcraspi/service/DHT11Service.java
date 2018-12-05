package com.robmelfi.rcraspi.service;

import com.robmelfi.rcraspi.sensor.dto.DHT11DataDTO;

public interface DHT11Service {

    DHT11DataDTO readTempHum(int pin);

    void storeTempHum(int pin);
}
