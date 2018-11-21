package com.robmelfi.rcraspi.web.rest;

import com.robmelfi.rcraspi.RcraspiApp;
import com.robmelfi.rcraspi.service.TestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Test class for the TestResource REST controller.
 *
 * @see TestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RcraspiApp.class)
public class TestResourceIntTest {

    private MockMvc restMockMvc;

    @Autowired
    private TestService testService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);;

        TestResource testResource = new TestResource(testService);
        restMockMvc = MockMvcBuilders
            .standaloneSetup(testResource)
            .build();

    }

    /**
     * Test test
     */
    @Test
    public void on() throws Exception {

        restMockMvc.perform(get("/api/test/on"))
            .andExpect(status().isOk());
    }

    /**
     * Test test
     */
    @Test
    public void off() throws Exception {
        restMockMvc.perform(get("/api/test/off"))
            .andExpect(status().isOk());
    }
}
