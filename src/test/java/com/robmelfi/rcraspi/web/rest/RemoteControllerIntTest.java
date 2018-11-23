//package com.robmelfi.rcraspi.web.rest;
//
//import com.robmelfi.rcraspi.RcraspiApp;
//import com.robmelfi.rcraspi.service.GpioService;
//import com.robmelfi.rcraspi.service.RemoteControllerService;
//import com.robmelfi.rcraspi.service.impl.RemoteControllerServiceImpl;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.env.MockEnvironment;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
///**
// * Test class for the TestResource REST controller.
// *
// * @see RemoteController
// */
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = RcraspiApp.class)
//public class RemoteControllerIntTest {
//
//    private MockMvc restMockMvc;
//
//    // private MockEnvironment env;
//    @Autowired
//    private GpioService gpioService;
//
//    @Autowired
//    private RemoteControllerService remoteControllerService;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);;
//
//        RemoteController remoteController = new RemoteController(remoteControllerService);
//        RemoteControllerService remoteControllerService = new RemoteControllerServiceImpl(gpioService);
//        restMockMvc = MockMvcBuilders
//            .standaloneSetup(remoteController, remoteControllerService)
//            .build();
//
//    }
//
//    /**
//     * Test on
//     */
//    @Test
//    public void setHigh() throws Exception {
//
//        restMockMvc.perform(get("/api/rc/high/GPIO_00"))
//            .andExpect(status().isOk());
//    }
//
//    /**
//     * Test off
//     */
//    @Test
//    public void setLow() throws Exception {
//        restMockMvc.perform(get("/api/rc/low"))
//            .andExpect(status().isOk());
//    }
//
//    /**
//     * Test toggle
//     */
//    @Test
//    public void toggle() throws Exception {
//        restMockMvc.perform(get("/api/rc/toggle"))
//            .andExpect(status().isOk());
//    }
//}
