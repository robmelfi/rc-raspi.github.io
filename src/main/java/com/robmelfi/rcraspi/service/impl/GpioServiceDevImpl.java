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

    private boolean status = false;

    public GpioServiceDevImpl(ControllerRepository controllerRepository, PinRepository pinRepository) {
        this.controllerRepository = controllerRepository;
        this.pinRepository = pinRepository;

        log.info("GpioFactory.getInstance()");
        gpioPinDigitalOutputs = new HashMap<>();
        loadController();
    }

    @Override
    public void setHigh(String pinName){
        status = true;
        log.debug("[DEV] - setHigh pin {}", getOutputPin(pinName));
    }

    @Override
    public void setLow(String pinName){
        status = false;
        log.debug("[DEV] - setLow pin {}", getOutputPin(pinName));
    }

    @Override
    public void toggle(String pinName) {
        status = !status;
        log.debug("[DEV] - toggle pin {}", getOutputPin(pinName));
    }

    @Override
    public boolean getState(String pinName) {
        log.debug("[DEV] - state pin {} - {}", getOutputPin(pinName), status);
        return status;
    }

    @Override
    public void addController(Controller c) {
        com.robmelfi.rcraspi.domain.Pin pin = pinRepository.findById(c.getPin().getId()).get();
        if (c.getMode().equals(IO.OUTPUT)) {
                String gpioPin = getRaspiPin(pin.getName());
                gpioPinDigitalOutputs.put(pin.getName(), gpioPin);
        } else {
            // TODO INPUT
        }
    }

    @Override
    public void removeController(Long id) {
        com.robmelfi.rcraspi.domain.Controller controller = controllerRepository.findById(id).get();
        gpioPinDigitalOutputs.remove(controller.getPin().getName());
    }

    private void loadController() {
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
            case "GPIO_04":
                return "GPIO_04";
            case "GPIO_05":
                return "GPIO_05";
            case "GPIO_06":
                return "GPIO_06";
            case "GPIO_07":
                return "GPIO_07";
            case "GPIO_08":
                return "GPIO_08";
            case "GPIO_09":
                return "GPIO_09";
            case "GPIO_10":
                return "GPIO_10";
            case "GPIO_11":
                return "GPIO_11";
            case "GPIO_12":
                return "GPIO_12";
            case "GPIO_13":
                return "GPIO_13";
            case "GPIO_14":
                return "GPIO_14";
            case "GPIO_15":
                return "GPIO_15";
            case "GPIO_16":
                return "GPIO_16";
            case "GPIO_17":
                return "GPIO_17";
            case "GPIO_18":
                return "GPIO_18";
            case "GPIO_19":
                return "GPIO_19";
            case "GPIO_20":
                return "GPIO_20";
            case "GPIO_21":
                return "GPIO_21";
            case "GPIO_22":
                return "GPIO_22";
            case "GPIO_23":
                return "GPIO_23";
            case "GPIO_24":
                return "GPIO_24";
            case "GPIO_25":
                return "GPIO_25";
            case "GPIO_26":
                return "GPIO_26";
            case "GPIO_27":
                return "GPIO_27";
            case "GPIO_28":
                return "GPIO_28";
            case "GPIO_29":
                return "GPIO_29";
            case "GPIO_30":
                return "GPIO_30";
            case "GPIO_31":
                return "GPIO_31";
            default:
                return null;
        }
    }
}
