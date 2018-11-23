package com.robmelfi.rcraspi.service.impl;

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
@Profile(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
public class GpioServiceDevImpl implements GpioService {

    private final Logger log = LoggerFactory.getLogger(GpioServiceDevImpl.class);

    // final private GpioController gpio;

    final private Map<String, String> gpioPinDigitalOutputs;

    private ControllerRepository controllerRepository;

    private PinRepository pinRepository;

    public GpioServiceDevImpl(ControllerRepository controllerRepository, PinRepository pinRepository) {
        this.controllerRepository = controllerRepository;
        this.pinRepository = pinRepository;

        log.info("GpioFactory.getInstance()");
        // gpio = GpioFactory.getInstance();
        gpioPinDigitalOutputs = new HashMap<>();
        initialLoad();
    }

    @Override
    public void setHigh(String pinName){
        log.debug(getOutputPin(pinName) + "HIGH");
    }

    @Override
    public void setLow(String pinName){
        log.debug(getOutputPin(pinName) + "LOW");
    }

    @Override
    public void addController(Controller c) {
        com.robmelfi.rcraspi.domain.Pin pin = pinRepository.findById(c.getPin().getId()).get();
        if (c.getMode().equals(IO.OUTPUT)) {
            //GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(getRaspiPin(c.getPin().getName()));
            gpioPinDigitalOutputs.put(c.getPin().getName(), pin.getName());
        } else {
            // TODO INPUT
        }
    }

    @Override
    public void removeController(Long id) {
        com.robmelfi.rcraspi.domain.Controller controller = controllerRepository.findById(id).get();
        gpioPinDigitalOutputs.remove(controller.getPin().getName());
    }

    private void initialLoad() {
        List<Controller> controllers = controllerRepository.findAll();
        for (Controller c: controllers) {
            addController(c);
        }
    }

    private String getOutputPin(String name) {
        return gpioPinDigitalOutputs.get(name);
    }

    private String getRaspiPin(String pin) {
        switch (pin) {
            case "GPIO_00":
                return "GPIO_00";
            case "GPIO_01":
                return "GPIO_01";
            case "GPIO_02":
                return "GPIO_02";
            case "GPIO_03":
                return "GPIO_03";
            default:
                return null;
        }
    }
}
