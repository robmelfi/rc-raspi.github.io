package com.robmelfi.rcraspi.service.impl;

import com.robmelfi.rcraspi.service.GpioService;
import com.robmelfi.rcraspi.service.RemoteControllerService;
import io.github.jhipster.config.JHipsterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Profile(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
public class RemoteControllerServiceDevImpl implements RemoteControllerService {

    private final Logger log = LoggerFactory.getLogger(RemoteControllerServiceDevImpl.class);

    private GpioService gpioService;

    public RemoteControllerServiceDevImpl(GpioService gpioService) {
        this.gpioService = gpioService;
    }

    @Override
    public void setHigh(String pin) {
        gpioService.setHigh(pin);
    }

    @Override
    public void setLow(String pin) {
        gpioService.setLow(pin);
    }

    @Override
    public void toggle(String pin) {
        gpioService.toggle(pin);
    }

    @Override
    public boolean getState(String pin) {
        return gpioService.getState(pin);
    }
}
