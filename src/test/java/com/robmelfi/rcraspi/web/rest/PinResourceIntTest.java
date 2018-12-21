package com.robmelfi.rcraspi.web.rest;

import com.robmelfi.rcraspi.RcraspiApp;

import com.robmelfi.rcraspi.domain.Pin;
import com.robmelfi.rcraspi.repository.PinRepository;
import com.robmelfi.rcraspi.service.PinService;
import com.robmelfi.rcraspi.service.dto.PinDTO;
import com.robmelfi.rcraspi.service.mapper.PinMapper;
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

/**
 * Test class for the PinResource REST controller.
 *
 * @see PinResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RcraspiApp.class)
public class PinResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private PinMapper pinMapper;

    @Autowired
    private PinService pinService;

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

    private MockMvc restPinMockMvc;

    private Pin pin;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PinResource pinResource = new PinResource(pinService);
        this.restPinMockMvc = MockMvcBuilders.standaloneSetup(pinResource)
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
    public static Pin createEntity(EntityManager em) {
        Pin pin = new Pin()
            .name(DEFAULT_NAME);
        return pin;
    }

    @Before
    public void initTest() {
        pin = createEntity(em);
    }

    @Test
    @Transactional
    public void createPin() throws Exception {
        int databaseSizeBeforeCreate = pinRepository.findAll().size();

        // Create the Pin
        PinDTO pinDTO = pinMapper.toDto(pin);
        restPinMockMvc.perform(post("/api/pins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pinDTO)))
            .andExpect(status().isCreated());

        // Validate the Pin in the database
        List<Pin> pinList = pinRepository.findAll();
        assertThat(pinList).hasSize(databaseSizeBeforeCreate + 1);
        Pin testPin = pinList.get(pinList.size() - 1);
        assertThat(testPin.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createPinWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pinRepository.findAll().size();

        // Create the Pin with an existing ID
        pin.setId(1L);
        PinDTO pinDTO = pinMapper.toDto(pin);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPinMockMvc.perform(post("/api/pins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pinDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pin in the database
        List<Pin> pinList = pinRepository.findAll();
        assertThat(pinList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPins() throws Exception {
        // Initialize the database
        pinRepository.saveAndFlush(pin);

        // Get all the pinList
        restPinMockMvc.perform(get("/api/pins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pin.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getPin() throws Exception {
        // Initialize the database
        pinRepository.saveAndFlush(pin);

        // Get the pin
        restPinMockMvc.perform(get("/api/pins/{id}", pin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pin.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPin() throws Exception {
        // Get the pin
        restPinMockMvc.perform(get("/api/pins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePin() throws Exception {
        // Initialize the database
        pinRepository.saveAndFlush(pin);

        int databaseSizeBeforeUpdate = pinRepository.findAll().size();

        // Update the pin
        Pin updatedPin = pinRepository.findById(pin.getId()).get();
        // Disconnect from session so that the updates on updatedPin are not directly saved in db
        em.detach(updatedPin);
        updatedPin
            .name(UPDATED_NAME);
        PinDTO pinDTO = pinMapper.toDto(updatedPin);

        restPinMockMvc.perform(put("/api/pins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pinDTO)))
            .andExpect(status().isOk());

        // Validate the Pin in the database
        List<Pin> pinList = pinRepository.findAll();
        assertThat(pinList).hasSize(databaseSizeBeforeUpdate);
        Pin testPin = pinList.get(pinList.size() - 1);
        assertThat(testPin.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingPin() throws Exception {
        int databaseSizeBeforeUpdate = pinRepository.findAll().size();

        // Create the Pin
        PinDTO pinDTO = pinMapper.toDto(pin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPinMockMvc.perform(put("/api/pins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pinDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pin in the database
        List<Pin> pinList = pinRepository.findAll();
        assertThat(pinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePin() throws Exception {
        // Initialize the database
        pinRepository.saveAndFlush(pin);

        int databaseSizeBeforeDelete = pinRepository.findAll().size();

        // Get the pin
        restPinMockMvc.perform(delete("/api/pins/{id}", pin.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Pin> pinList = pinRepository.findAll();
        assertThat(pinList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pin.class);
        Pin pin1 = new Pin();
        pin1.setId(1L);
        Pin pin2 = new Pin();
        pin2.setId(pin1.getId());
        assertThat(pin1).isEqualTo(pin2);
        pin2.setId(2L);
        assertThat(pin1).isNotEqualTo(pin2);
        pin1.setId(null);
        assertThat(pin1).isNotEqualTo(pin2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PinDTO.class);
        PinDTO pinDTO1 = new PinDTO();
        pinDTO1.setId(1L);
        PinDTO pinDTO2 = new PinDTO();
        assertThat(pinDTO1).isNotEqualTo(pinDTO2);
        pinDTO2.setId(pinDTO1.getId());
        assertThat(pinDTO1).isEqualTo(pinDTO2);
        pinDTO2.setId(2L);
        assertThat(pinDTO1).isNotEqualTo(pinDTO2);
        pinDTO1.setId(null);
        assertThat(pinDTO1).isNotEqualTo(pinDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pinMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pinMapper.fromId(null)).isNull();
    }
}
