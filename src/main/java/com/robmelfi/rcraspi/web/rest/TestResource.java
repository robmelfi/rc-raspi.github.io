package com.robmelfi.rcraspi.web.rest;

import com.robmelfi.rcraspi.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestResource controller
 */
@RestController
@RequestMapping("/api/test")
public class TestResource {

    private final Logger log = LoggerFactory.getLogger(TestResource.class);

    private TestService testService;

    public TestResource(TestService testService) {
        this.testService = testService;
    }

    /**
    * GET test
    */
    @GetMapping("/on")
    public String on() {
        return testService.on();
    }

    /**
    * GET test
    */
    @GetMapping("/off")
    public String off() {
        return testService.off();
    }

}
