package com.robmelfi.rcraspi.sensor;

import com.robmelfi.rcraspi.sensor.enumeration.SensorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SensorStrategyService {

    private final Logger log = LoggerFactory.getLogger(SensorStrategyService.class);

    private final SensorStrategyFactory sensorStrategyFactory;

    public SensorStrategyService(SensorStrategyFactory sensorStrategyFactory) {
        this.sensorStrategyFactory = sensorStrategyFactory;
    }

    public void enableSensor(String sensorName, int pin) {
        log.info("SensorStrategyService.enableSensor({}, {}) - type of sensor {}", sensorName, pin, getSensorType(sensorName).toString());
        SensorStrategy sensorStrategy = sensorStrategyFactory.getStrategy(getSensorType(sensorName));
        sensorStrategy.setPin(pin);
        sensorStrategy.enable();
    }

    public void disableSensor(String sensorName, int pin) {
        log.info("SensorStrategyService.disableSensor({}, {} - type of sensor {})", sensorName, pin, getSensorType(sensorName).toString());
        SensorStrategy sensorStrategy = sensorStrategyFactory.getStrategy(getSensorType(sensorName));
        sensorStrategy.disable();
    }

    private SensorType getSensorType(String name) {
        switch (name) {
            case "DHT11":
                return SensorType.DHT11;
            default:
                return null;
        }
    }
}
