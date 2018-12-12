//package com.robmelfi.rcraspi.config;
//
//import com.robmelfi.rcraspi.repository.ControllerRepository;
//import com.robmelfi.rcraspi.repository.PinRepository;
//import com.robmelfi.rcraspi.service.GpioService;
//import com.robmelfi.rcraspi.service.RemoteControllerService;
//import com.robmelfi.rcraspi.service.impl.GpioServiceDevImpl;
//import com.robmelfi.rcraspi.service.impl.RemoteControllerServiceDevImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class TestConfig {
//
//    @Autowired
//    ControllerRepository controllerRepository;
//
//    @Autowired
//    PinRepository pinRepository;
//
//    @Bean
//    public RemoteControllerService getTestService() {
//        return new RemoteControllerServiceDevImpl(new GpioServiceDevImpl(controllerRepository, pinRepository));
//    }
//}
