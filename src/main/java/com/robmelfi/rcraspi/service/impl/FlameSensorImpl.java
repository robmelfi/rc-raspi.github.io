package com.robmelfi.rcraspi.service.impl;

import com.pi4j.io.gpio.Pin;
import com.robmelfi.rcraspi.sensor.FlameSensor;
import com.robmelfi.rcraspi.service.FlameSensorService;
import io.github.jhipster.config.JHipsterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
public class FlameSensorImpl implements FlameSensorService {

    private final Logger log = LoggerFactory.getLogger(FlameSensorImpl.class);

    private final FlameSensor flameSensor;

    public FlameSensorImpl(FlameSensor flameSensor) {
        this.flameSensor = flameSensor;
    }

    @Override
    public boolean flameDetected() {
        return this.flameSensor.flameDetected();
    }

    @Override
    public void setPin(Pin pin) {
        this.flameSensor.setPin(pin);
    }
}
