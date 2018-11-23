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
    public void setHigh(String pin) {
        log.info("setHigh pin {}", pin);
        gpioService.setHigh(pin);
    }

    @Override
    public void setLow(String pin) {
        log.info("setHigh low {}", pin);
        gpioService.setLow(pin);
    }

    @Override
    public void toggle(String pin) {
        log.info("toggle pin {}", pin);
        gpioService.toggle(pin);
    }

    @Override
    public boolean getState(String pin) {
        log.debug(" get pin state {}", pin);
        return  gpioService.getState(pin);
    }
}
