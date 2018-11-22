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
    public void on() {
        log.debug("ON - DEV");
    }

    @Override
    public void off() {
        log.debug("OFF - DEV");
    }

    @Override
    public void toggle() {
        log.debug("TOGGLE - DEV");
    }
}
