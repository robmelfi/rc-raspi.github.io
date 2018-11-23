package com.robmelfi.rcraspi.service.impl;

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

    @Override
    public void setHigh(String pin) {
        log.debug("[DEV] - setHigh pin {}", pin);
    }

    @Override
    public void setLow(String pin) {
        log.debug("[DEV] - setLow pin {}", pin);
    }

    @Override
    public void toggle(String pin) {
        log.debug("[DEV] - toggle pin {}", pin);
    }

    @Override
    public boolean getState(String pin) {
        log.debug("[DEV] - get pin state {}", pin);
        return false;
    }
}
