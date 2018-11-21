package com.robmelfi.rcraspi.config;

import com.robmelfi.rcraspi.service.TestService;
import com.robmelfi.rcraspi.service.impl.TestServiceDevImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public TestService getTestService() {
        return new TestServiceDevImpl();
    }
}
