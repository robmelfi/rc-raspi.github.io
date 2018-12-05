package com.robmelfi.rcraspi.web.rest;

import com.robmelfi.rcraspi.sensor.dto.DHT11DataDTO;
import com.robmelfi.rcraspi.service.DHT11Service;
import com.robmelfi.rcraspi.service.RemoteControllerService;
import com.robmelfi.rcraspi.service.scheduled.ScheduleDHT11Controller;
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

    private final RemoteControllerService remoteControllerService;

    private final DHT11Service dht11Service;

    private final ScheduleDHT11Controller scheduleDHT11Controller;

    public RemoteController(RemoteControllerService remoteControllerService, DHT11Service dht11Service, ScheduleDHT11Controller scheduleDHT11Controller) {
        this.remoteControllerService = remoteControllerService;
        this.dht11Service = dht11Service;
        this.scheduleDHT11Controller = scheduleDHT11Controller;
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

    @GetMapping("/dht11/{pin}")
    public DHT11DataDTO getTemp(@PathVariable int pin) {
        return dht11Service.readTempHum(pin);
    }

    @GetMapping("/start")
    public void start() {
        scheduleDHT11Controller.start(6, 2000);
    }

    @GetMapping("/stop")
    public void stop() {
        scheduleDHT11Controller.stop();
    }
}
