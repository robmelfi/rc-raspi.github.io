package com.robmelfi.rcraspi.web.rest;

import com.robmelfi.rcraspi.RcraspiApp;

import com.robmelfi.rcraspi.domain.Controller;
import com.robmelfi.rcraspi.repository.ControllerRepository;
import com.robmelfi.rcraspi.service.ControllerService;
import com.robmelfi.rcraspi.service.dto.ControllerDTO;
import com.robmelfi.rcraspi.service.mapper.ControllerMapper;
import com.robmelfi.rcraspi.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static com.robmelfi.rcraspi.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.robmelfi.rcraspi.domain.enumeration.IO;
/**
 * Test class for the ControllerResource REST controller.
 *
 * @see ControllerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RcraspiApp.class)
public class ControllerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final IO DEFAULT_MODE = IO.INPUT;
    private static final IO UPDATED_MODE = IO.OUTPUT;

    private static final Boolean DEFAULT_STATE = false;
    private static final Boolean UPDATED_STATE = true;

    @Autowired
    private ControllerRepository controllerRepository;

    @Autowired
    private ControllerMapper controllerMapper;

    @Autowired
    private ControllerService controllerService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restControllerMockMvc;

    private Controller controller;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ControllerResource controllerResource = new ControllerResource(controllerService);
        this.restControllerMockMvc = MockMvcBuilders.standaloneSetup(controllerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Controller createEntity(EntityManager em) {
        Controller controller = new Controller()
            .name(DEFAULT_NAME)
            .mode(DEFAULT_MODE)
            .state(DEFAULT_STATE);
        return controller;
    }

    @Before
    public void initTest() {
        controller = createEntity(em);
    }

    @Test
    @Transactional
    public void createController() throws Exception {
        int databaseSizeBeforeCreate = controllerRepository.findAll().size();

        // Create the Controller
        ControllerDTO controllerDTO = controllerMapper.toDto(controller);
        restControllerMockMvc.perform(post("/api/controllers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(controllerDTO)))
            .andExpect(status().isCreated());

        // Validate the Controller in the database
        List<Controller> controllerList = controllerRepository.findAll();
        assertThat(controllerList).hasSize(databaseSizeBeforeCreate + 1);
        Controller testController = controllerList.get(controllerList.size() - 1);
        assertThat(testController.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testController.getMode()).isEqualTo(DEFAULT_MODE);
        assertThat(testController.isState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    public void createControllerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = controllerRepository.findAll().size();

        // Create the Controller with an existing ID
        controller.setId(1L);
        ControllerDTO controllerDTO = controllerMapper.toDto(controller);

        // An entity with an existing ID cannot be created, so this API call must fail
        restControllerMockMvc.perform(post("/api/controllers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(controllerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Controller in the database
        List<Controller> controllerList = controllerRepository.findAll();
        assertThat(controllerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllControllers() throws Exception {
        // Initialize the database
        controllerRepository.saveAndFlush(controller);

        // Get all the controllerList
        restControllerMockMvc.perform(get("/api/controllers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(controller.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].mode").value(hasItem(DEFAULT_MODE.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getController() throws Exception {
        // Initialize the database
        controllerRepository.saveAndFlush(controller);

        // Get the controller
        restControllerMockMvc.perform(get("/api/controllers/{id}", controller.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(controller.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.mode").value(DEFAULT_MODE.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingController() throws Exception {
        // Get the controller
        restControllerMockMvc.perform(get("/api/controllers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateController() throws Exception {
        // Initialize the database
        controllerRepository.saveAndFlush(controller);

        int databaseSizeBeforeUpdate = controllerRepository.findAll().size();

        // Update the controller
        Controller updatedController = controllerRepository.findById(controller.getId()).get();
        // Disconnect from session so that the updates on updatedController are not directly saved in db
        em.detach(updatedController);
        updatedController
            .name(UPDATED_NAME)
            .mode(UPDATED_MODE)
            .state(UPDATED_STATE);
        ControllerDTO controllerDTO = controllerMapper.toDto(updatedController);

        restControllerMockMvc.perform(put("/api/controllers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(controllerDTO)))
            .andExpect(status().isOk());

        // Validate the Controller in the database
        List<Controller> controllerList = controllerRepository.findAll();
        assertThat(controllerList).hasSize(databaseSizeBeforeUpdate);
        Controller testController = controllerList.get(controllerList.size() - 1);
        assertThat(testController.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testController.getMode()).isEqualTo(UPDATED_MODE);
        assertThat(testController.isState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    public void updateNonExistingController() throws Exception {
        int databaseSizeBeforeUpdate = controllerRepository.findAll().size();

        // Create the Controller
        ControllerDTO controllerDTO = controllerMapper.toDto(controller);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restControllerMockMvc.perform(put("/api/controllers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(controllerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Controller in the database
        List<Controller> controllerList = controllerRepository.findAll();
        assertThat(controllerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteController() throws Exception {
        // Initialize the database
        controllerRepository.saveAndFlush(controller);

        int databaseSizeBeforeDelete = controllerRepository.findAll().size();

        // Get the controller
        restControllerMockMvc.perform(delete("/api/controllers/{id}", controller.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Controller> controllerList = controllerRepository.findAll();
        assertThat(controllerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Controller.class);
        Controller controller1 = new Controller();
        controller1.setId(1L);
        Controller controller2 = new Controller();
        controller2.setId(controller1.getId());
        assertThat(controller1).isEqualTo(controller2);
        controller2.setId(2L);
        assertThat(controller1).isNotEqualTo(controller2);
        controller1.setId(null);
        assertThat(controller1).isNotEqualTo(controller2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ControllerDTO.class);
        ControllerDTO controllerDTO1 = new ControllerDTO();
        controllerDTO1.setId(1L);
        ControllerDTO controllerDTO2 = new ControllerDTO();
        assertThat(controllerDTO1).isNotEqualTo(controllerDTO2);
        controllerDTO2.setId(controllerDTO1.getId());
        assertThat(controllerDTO1).isEqualTo(controllerDTO2);
        controllerDTO2.setId(2L);
        assertThat(controllerDTO1).isNotEqualTo(controllerDTO2);
        controllerDTO1.setId(null);
        assertThat(controllerDTO1).isNotEqualTo(controllerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(controllerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(controllerMapper.fromId(null)).isNull();
    }
}
