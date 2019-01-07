package com.robmelfi.rcraspi.service.impl;

import com.pi4j.io.gpio.*;
import com.robmelfi.rcraspi.domain.Controller;
import com.robmelfi.rcraspi.domain.Sensor;
import com.robmelfi.rcraspi.domain.enumeration.IO;
import com.robmelfi.rcraspi.repository.ControllerRepository;
import com.robmelfi.rcraspi.repository.PinRepository;
import com.robmelfi.rcraspi.repository.SensorRepository;
import com.robmelfi.rcraspi.sensor.SensorStrategyService;
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

    private final GpioController gpio;

    private final Map<String, GpioPinDigitalOutput> gpioPinDigitalOutputs;

    private final ControllerRepository controllerRepository;

    private final PinRepository pinRepository;

    private final SensorRepository sensorRepository;

    private final SensorStrategyService sensorStrategyService;

    public GpioServiceImpl(ControllerRepository controllerRepository, PinRepository pinRepository, SensorRepository sensorRepository, SensorStrategyService sensorStrategyService) {
        this.controllerRepository = controllerRepository;
        this.pinRepository = pinRepository;
        this.sensorRepository = sensorRepository;
        this.sensorStrategyService = sensorStrategyService;

        gpio = GpioFactory.getInstance();
        gpioPinDigitalOutputs = new HashMap<>();
        loadController();
    }

    @Override
    public void setHigh(String pinName){
        getOutputPin(pinName).high();
        updateController(pinName, true);
    }

    @Override
    public void setLow(String pinName){
        getOutputPin(pinName).low();
        updateController(pinName, false);
    }

    @Override
    public void toggle(String pinName) {
        Boolean actualState = this.getState(pinName);
        getOutputPin(pinName).toggle();
        updateController(pinName, !actualState);
    }

    @Override
    public boolean getState(String pinName) {
        return getOutputPin(pinName).getState().isHigh();
    }

    private void updateController(String pinName, Boolean state) {
        Controller c = controllerRepository.findByPinName(pinName);
        c.setState(state);
        controllerRepository.save(c);
    }

    @Override
    public void addController(Controller c, boolean update, String oldPinName) {
        com.robmelfi.rcraspi.domain.Pin pin = pinRepository.findById(c.getPin().getId()).get();
        PinState state = c.getState() ? PinState.HIGH : PinState.LOW;
        if (c.getMode().equals(IO.OUTPUT)) {
            if(!update) {
                GpioPinDigitalOutput gpioPin = gpio.provisionDigitalOutputPin(getRaspiPin(pin.getName()), state);
                gpioPinDigitalOutputs.put(pin.getName(), gpioPin);
            } else {
                GpioPinDigitalOutput p = gpioPinDigitalOutputs.remove(oldPinName);
                gpio.unprovisionPin(p);
                GpioPinDigitalOutput gpioPin = gpio.provisionDigitalOutputPin(getRaspiPin(pin.getName()), state);
                gpioPinDigitalOutputs.put(pin.getName(), gpioPin);
            }
        } else {
            Sensor sensor = sensorRepository.findById(c.getSensor().getId()).get();
            sensorStrategyService.enableSensor(sensor.getName(), getRaspiPin(pin.getName()));
        }
    }

    @Override
    public void removeController(Long id) {
        com.robmelfi.rcraspi.domain.Controller controller = controllerRepository.findById(id).get();
        if (controller.getMode().equals(IO.OUTPUT)) {
            GpioPinDigitalOutput p = gpioPinDigitalOutputs.get(controller.getPin().getName());
            gpio.unprovisionPin(p);
            gpioPinDigitalOutputs.remove(controller.getPin().getName());
        } else {
            Sensor sensor = sensorRepository.findById(controller.getSensor().getId()).get();
            sensorStrategyService.disableSensor(sensor.getName());
        }


    }

    private void loadController() {
        List<Controller> controllers = controllerRepository.findAll();
        for (Controller c: controllers) {
            addController(c, false, null);
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
            case "GPIO_04":
                return RaspiPin.GPIO_04;
            case "GPIO_05":
                return RaspiPin.GPIO_05;
            case "GPIO_06":
                return RaspiPin.GPIO_06;
            case "GPIO_07":
                return RaspiPin.GPIO_07;
            case "GPIO_08":
                return RaspiPin.GPIO_08;
            case "GPIO_09":
                return RaspiPin.GPIO_09;
            case "GPIO_10":
                return RaspiPin.GPIO_10;
            case "GPIO_11":
                return RaspiPin.GPIO_11;
            case "GPIO_12":
                return RaspiPin.GPIO_12;
            case "GPIO_13":
                return RaspiPin.GPIO_13;
            case "GPIO_14":
                return RaspiPin.GPIO_14;
            case "GPIO_15":
                return RaspiPin.GPIO_15;
            case "GPIO_16":
                return RaspiPin.GPIO_16;
            case "GPIO_17":
                return RaspiPin.GPIO_17;
            case "GPIO_18":
                return RaspiPin.GPIO_18;
            case "GPIO_19":
                return RaspiPin.GPIO_19;
            case "GPIO_20":
                return RaspiPin.GPIO_20;
            case "GPIO_21":
                return RaspiPin.GPIO_21;
            case "GPIO_22":
                return RaspiPin.GPIO_22;
            case "GPIO_23":
                return RaspiPin.GPIO_23;
            case "GPIO_24":
                return RaspiPin.GPIO_24;
            case "GPIO_25":
                return RaspiPin.GPIO_25;
            case "GPIO_26":
                return RaspiPin.GPIO_26;
            case "GPIO_27":
                return RaspiPin.GPIO_27;
            case "GPIO_28":
                return RaspiPin.GPIO_28;
            case "GPIO_29":
                return RaspiPin.GPIO_29;
            case "GPIO_30":
                return RaspiPin.GPIO_30;
            case "GPIO_31":
                return RaspiPin.GPIO_31;
            default:
                return null;
        }
    }
}
