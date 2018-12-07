package com.robmelfi.rcraspi.sensor;

import com.robmelfi.rcraspi.sensor.enumeration.SensorType;
import com.robmelfi.rcraspi.sensor.impl.DHT11Manager;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class SensorStrategyFactory {

    private Map<SensorType, SensorStrategy> sensorStrategiesMap = new EnumMap<>(SensorType.class);

    private final DHT11Manager dht11Manager;

    public SensorStrategyFactory(DHT11Manager dht11Manager) {
        this.dht11Manager = dht11Manager;
        initStrategies();
    }

    public SensorStrategy getStrategy(SensorType sensorType) {
        if (sensorType == null || !sensorStrategiesMap.containsKey(sensorType)) {
            throw new IllegalArgumentException("Invalid " + sensorType);
        }
        return sensorStrategiesMap.get(sensorType);
    }

    private void initStrategies() {
        sensorStrategiesMap.put(SensorType.DHT11, dht11Manager);
    }
}
