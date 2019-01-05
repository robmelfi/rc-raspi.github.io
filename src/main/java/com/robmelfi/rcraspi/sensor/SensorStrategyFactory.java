package com.robmelfi.rcraspi.sensor;

import com.robmelfi.rcraspi.sensor.enumeration.SensorType;
import com.robmelfi.rcraspi.sensor.impl.DHT11Manager;
import com.robmelfi.rcraspi.sensor.impl.FlameSensorManager;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class SensorStrategyFactory {

    private Map<SensorType, SensorStrategy> sensorStrategiesMap = new EnumMap<>(SensorType.class);

    private final DHT11Manager dht11Manager;

    private final FlameSensorManager flameSensorManager;

    public SensorStrategyFactory(DHT11Manager dht11Manager, FlameSensorManager flameSensorManager) {
        this.dht11Manager = dht11Manager;
        this.flameSensorManager = flameSensorManager;
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
        sensorStrategiesMap.put(SensorType.FLAME_SENSOR, flameSensorManager);
    }
}
