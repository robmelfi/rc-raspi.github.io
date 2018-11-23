package com.robmelfi.rcraspi.service.impl;

import com.pi4j.io.gpio.*;
import com.robmelfi.rcraspi.domain.Controller;
import com.robmelfi.rcraspi.domain.enumeration.IO;
import com.robmelfi.rcraspi.repository.ControllerRepository;
import com.robmelfi.rcraspi.repository.PinRepository;
import com.robmelfi.rcraspi.service.GpioService;
import io.github.jhipster.config.JHipsterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Profile(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
public class GpioServiceImpl implements GpioService {

    private final Logger log = LoggerFactory.getLogger(GpioServiceImpl.class);

    final private GpioController gpio;

    final private Map<String, GpioPinDigitalOutput> gpioPinDigitalOutputs;

    private ControllerRepository controllerRepository;

    private PinRepository pinRepository;

    public GpioServiceImpl(ControllerRepository controllerRepository, PinRepository pinRepository) {
        this.controllerRepository = controllerRepository;
        this.pinRepository = pinRepository;

        gpio = GpioFactory.getInstance();
        gpioPinDigitalOutputs = new HashMap<>();
        initialLoad();
    }

    @Override
    public void setHigh(String pinName){
        getOutputPin(pinName).high();
    }

    @Override
    public void setLow(String pinName){
        getOutputPin(pinName).low();
    }

    @Override
    public void addController(Controller c) {
        com.robmelfi.rcraspi.domain.Pin pin = pinRepository.findById(c.getPin().getId()).get();
        if (c.getMode().equals(IO.OUTPUT)) {
            GpioPinDigitalOutput gpioPin = gpio.provisionDigitalOutputPin(getRaspiPin(pin.getName()));
            gpioPinDigitalOutputs.put(pin.getName(), gpioPin);
        } else {
            // TODO INPUT
        }
    }

    @Override
    public void removeController(Long id) {
        com.robmelfi.rcraspi.domain.Controller controller = controllerRepository.findById(id).get();
        GpioPinDigitalOutput p = gpioPinDigitalOutputs.get(controller.getPin().getName());
        gpio.unprovisionPin(p);
        gpioPinDigitalOutputs.remove(controller.getPin().getName());

    }

    private void initialLoad() {
        List<Controller> controllers = controllerRepository.findAll();
        for (Controller c: controllers) {
            addController(c);
        }
    }

    private GpioPinDigitalOutput getOutputPin(String name) {
        return gpioPinDigitalOutputs.get(name);
    }

    private Pin getRaspiPin(String pin) {
        switch (pin) {
            case "GPIO_00":
                return RaspiPin.GPIO_00;
            case "GPIO_01":
                return RaspiPin.GPIO_01;
            case "GPIO_02":
                return RaspiPin.GPIO_02;
            case "GPIO_03":
                return RaspiPin.GPIO_03;
            default:
                return null;
        }
    }
}
