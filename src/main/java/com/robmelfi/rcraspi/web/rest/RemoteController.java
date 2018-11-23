package com.robmelfi.rcraspi.web.rest;

import com.robmelfi.rcraspi.service.RemoteControllerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @GetMapping("/on/{pin}")
    public void on(@PathVariable String pin) {
        remoteControllerService.on(pin);
    }

    /**
    * GET off
    */
    @GetMapping("/off/{pin}")
    public void off(@PathVariable String pin) {
        remoteControllerService.off(pin);
    }

    /**
     * GET toggle
     */
    @GetMapping("/toggle/{pin}")
    public void toggle(@PathVariable String pin) {
        remoteControllerService.toggle(pin);
    }

}
