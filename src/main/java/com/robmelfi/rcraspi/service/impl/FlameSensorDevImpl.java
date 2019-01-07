package com.robmelfi.rcraspi.service.impl;

import com.pi4j.io.gpio.Pin;
import com.robmelfi.rcraspi.service.FlameSensorService;
import io.github.jhipster.config.JHipsterConstants;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
public class FlameSensorDevImpl implements FlameSensorService {

    private boolean flame = false;

    @Override
    public boolean flameDetected() {
        this.flame = !this.flame;
        return this.flame;
    }

    @Override
    public void setPin(Pin pin) {

    }
}
