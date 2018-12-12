package com.robmelfi.rcraspi.service.scheduled;

import com.robmelfi.rcraspi.service.DHT11Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledFuture;

@Service
public class ScheduleDHT11Controller {

    private final Logger log = LoggerFactory.getLogger(ScheduleDHT11Controller.class);

    private final DHT11Service dht11Service;

    private final TaskScheduler taskScheduler;

    private ScheduledFuture scheduledFuture;

    public ScheduleDHT11Controller(DHT11Service dht11Service, TaskScheduler taskScheduler) {
        this.dht11Service = dht11Service;
        this.taskScheduler = taskScheduler;
    }

    public void start(int pin, long rate) {
        scheduledFuture = taskScheduler.scheduleAtFixedRate(readHumTemp(pin), rate);
    }

    public void stop() {
        scheduledFuture.cancel(false);
    }

    private Runnable readHumTemp(int pin) {
        return () -> dht11Service.storeTempHum(pin);
    }
}
