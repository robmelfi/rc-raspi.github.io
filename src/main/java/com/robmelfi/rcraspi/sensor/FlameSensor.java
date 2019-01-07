package com.robmelfi.rcraspi.sensor;

import com.pi4j.io.gpio.*;
import io.github.jhipster.config.JHipsterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
public class FlameSensor {

    private final Logger log = LoggerFactory.getLogger(FlameSensor.class);

    private final GpioController gpio;
    private GpioPinDigitalInput pin;

    public FlameSensor() {
        gpio = GpioFactory.getInstance();
    }

    public void setPin(Pin p) {
        pin = gpio.provisionDigitalInputPin(p);
    }

    // return true if flame is detected
    public boolean flameDetected() {
        // if pin is low flame is detected
        return pin.isLow();
    }

}
