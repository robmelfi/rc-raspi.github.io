package com.robmelfi.rcraspi.web.rest;

import com.robmelfi.rcraspi.service.RemoteControllerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestResource controller
 */
@RestController
@RequestMapping("/api/rc")
public class RemoteController {

    private final Logger log = LoggerFactory.getLogger(RemoteController.class);

    private RemoteControllerService remoteControllerService;

    public RemoteController(RemoteControllerService remoteControllerService) {
        this.remoteControllerService = remoteControllerService;
    }

    /**
    * GET on
    */
    @GetMapping("/on")
    public void on() {
        remoteControllerService.on();
    }

    /**
    * GET off
    */
    @GetMapping("/off")
    public void off() {
        remoteControllerService.off();
    }

    /**
     * GET toggle
     */
    @GetMapping("/toggle")
    public void toggle() {
        remoteControllerService.toggle();
    }

}
