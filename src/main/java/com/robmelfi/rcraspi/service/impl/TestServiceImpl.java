package com.robmelfi.rcraspi.service.impl;

import com.robmelfi.rcraspi.service.TestService;
import io.github.jhipster.config.JHipsterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Profile(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
public class TestServiceImpl implements TestService {

    private final Logger log = LoggerFactory.getLogger(TestServiceImpl.class);

    @Override
    public String on() {
        return "on-prod";
    }

    @Override
    public String off() {
        return "off-prod";
    }
}
