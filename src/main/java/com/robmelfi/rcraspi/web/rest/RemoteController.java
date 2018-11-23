package com.robmelfi.rcraspi.web.rest;

import com.robmelfi.rcraspi.service.RemoteControllerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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
    * POST on
    */
    @PostMapping("/high/{pin}")
    public void setHigh(@PathVariable String pin) {
        remoteControllerService.setHigh(pin);
    }

    /**
    * POST off
    */
    @PostMapping("/low/{pin}")
    public void setLow(@PathVariable String pin) {
        remoteControllerService.setLow(pin);
    }

    /**
     * POST toggle
     */
    @PostMapping("/toggle/{pin}")
    public void toggle(@PathVariable String pin) {
        remoteControllerService.toggle(pin);
    }

    /**
     * GET pin state
     */
    @GetMapping("/state/{pin}") boolean getState(@PathVariable String pin) {
        return remoteControllerService.getState(pin);
    }

}
