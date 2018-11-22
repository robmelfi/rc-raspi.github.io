package com.robmelfi.rcraspi.config;

import com.robmelfi.rcraspi.service.RemoteControllerService;
import com.robmelfi.rcraspi.service.impl.RemoteControllerServiceDevImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public RemoteControllerService getTestService() {
        return new RemoteControllerServiceDevImpl();
    }
}
