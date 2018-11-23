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
@Profile(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
public class RemoteControllerServiceImpl implements RemoteControllerService {

    private final Logger log = LoggerFactory.getLogger(RemoteControllerServiceImpl.class);

    private GpioService gpioService;

    public RemoteControllerServiceImpl(GpioService gpioService) {
        this.gpioService = gpioService;
    }

    @Override
    public void on(String pin) {
        log.info("ON - PROD");
        gpioService.setHigh(pin);
    }

    @Override
    public void off(String pin) {
        log.info("OFF - PROD");
        gpioService.setLow(pin);
    }

    @Override
    public void toggle(String pin) {
        log.info("TOGGLE - PROD");
    }
}
