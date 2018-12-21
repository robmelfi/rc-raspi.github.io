package com.robmelfi.rcraspi.web.rest;

import com.robmelfi.rcraspi.RcraspiApp;

import com.robmelfi.rcraspi.domain.Humidity;
import com.robmelfi.rcraspi.repository.HumidityRepository;
import com.robmelfi.rcraspi.service.HumidityService;
import com.robmelfi.rcraspi.service.dto.HumidityDTO;
import com.robmelfi.rcraspi.service.mapper.HumidityMapper;
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

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;


import static com.robmelfi.rcraspi.web.rest.TestUtil.sameInstant;
import static com.robmelfi.rcraspi.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HumidityResource REST controller.
 *
 * @see HumidityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RcraspiApp.class)
public class HumidityResourceIntTest {

    private static final Float DEFAULT_VALUE = 1F;
    private static final Float UPDATED_VALUE = 2F;

    private static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private HumidityRepository humidityRepository;

    @Autowired
    private HumidityMapper humidityMapper;

    @Autowired
    private HumidityService humidityService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHumidityMockMvc;

    private Humidity humidity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HumidityResource humidityResource = new HumidityResource(humidityService);
        this.restHumidityMockMvc = MockMvcBuilders.standaloneSetup(humidityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Humidity createEntity(EntityManager em) {
        Humidity humidity = new Humidity()
            .value(DEFAULT_VALUE)
            .timestamp(DEFAULT_TIMESTAMP);
        return humidity;
    }

    @Before
    public void initTest() {
        humidity = createEntity(em);
    }

    @Test
    @Transactional
    public void createHumidity() throws Exception {
        int databaseSizeBeforeCreate = humidityRepository.findAll().size();

        // Create the Humidity
        HumidityDTO humidityDTO = humidityMapper.toDto(humidity);
        restHumidityMockMvc.perform(post("/api/humidities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(humidityDTO)))
            .andExpect(status().isCreated());

        // Validate the Humidity in the database
        List<Humidity> humidityList = humidityRepository.findAll();
        assertThat(humidityList).hasSize(databaseSizeBeforeCreate + 1);
        Humidity testHumidity = humidityList.get(humidityList.size() - 1);
        assertThat(testHumidity.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testHumidity.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    public void createHumidityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = humidityRepository.findAll().size();

        // Create the Humidity with an existing ID
        humidity.setId(1L);
        HumidityDTO humidityDTO = humidityMapper.toDto(humidity);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHumidityMockMvc.perform(post("/api/humidities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(humidityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Humidity in the database
        List<Humidity> humidityList = humidityRepository.findAll();
        assertThat(humidityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = humidityRepository.findAll().size();
        // set the field null
        humidity.setTimestamp(null);

        // Create the Humidity, which fails.
        HumidityDTO humidityDTO = humidityMapper.toDto(humidity);

        restHumidityMockMvc.perform(post("/api/humidities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(humidityDTO)))
            .andExpect(status().isBadRequest());

        List<Humidity> humidityList = humidityRepository.findAll();
        assertThat(humidityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHumidities() throws Exception {
        // Initialize the database
        humidityRepository.saveAndFlush(humidity);

        // Get all the humidityList
        restHumidityMockMvc.perform(get("/api/humidities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(humidity.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))));
    }
    
    @Test
    @Transactional
    public void getHumidity() throws Exception {
        // Initialize the database
        humidityRepository.saveAndFlush(humidity);

        // Get the humidity
        restHumidityMockMvc.perform(get("/api/humidities/{id}", humidity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(humidity.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.timestamp").value(sameInstant(DEFAULT_TIMESTAMP)));
    }

    @Test
    @Transactional
    public void getNonExistingHumidity() throws Exception {
        // Get the humidity
        restHumidityMockMvc.perform(get("/api/humidities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHumidity() throws Exception {
        // Initialize the database
        humidityRepository.saveAndFlush(humidity);

        int databaseSizeBeforeUpdate = humidityRepository.findAll().size();

        // Update the humidity
        Humidity updatedHumidity = humidityRepository.findById(humidity.getId()).get();
        // Disconnect from session so that the updates on updatedHumidity are not directly saved in db
        em.detach(updatedHumidity);
        updatedHumidity
            .value(UPDATED_VALUE)
            .timestamp(UPDATED_TIMESTAMP);
        HumidityDTO humidityDTO = humidityMapper.toDto(updatedHumidity);

        restHumidityMockMvc.perform(put("/api/humidities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(humidityDTO)))
            .andExpect(status().isOk());

        // Validate the Humidity in the database
        List<Humidity> humidityList = humidityRepository.findAll();
        assertThat(humidityList).hasSize(databaseSizeBeforeUpdate);
        Humidity testHumidity = humidityList.get(humidityList.size() - 1);
        assertThat(testHumidity.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testHumidity.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void updateNonExistingHumidity() throws Exception {
        int databaseSizeBeforeUpdate = humidityRepository.findAll().size();

        // Create the Humidity
        HumidityDTO humidityDTO = humidityMapper.toDto(humidity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHumidityMockMvc.perform(put("/api/humidities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(humidityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Humidity in the database
        List<Humidity> humidityList = humidityRepository.findAll();
        assertThat(humidityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHumidity() throws Exception {
        // Initialize the database
        humidityRepository.saveAndFlush(humidity);

        int databaseSizeBeforeDelete = humidityRepository.findAll().size();

        // Get the humidity
        restHumidityMockMvc.perform(delete("/api/humidities/{id}", humidity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Humidity> humidityList = humidityRepository.findAll();
        assertThat(humidityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Humidity.class);
        Humidity humidity1 = new Humidity();
        humidity1.setId(1L);
        Humidity humidity2 = new Humidity();
        humidity2.setId(humidity1.getId());
        assertThat(humidity1).isEqualTo(humidity2);
        humidity2.setId(2L);
        assertThat(humidity1).isNotEqualTo(humidity2);
        humidity1.setId(null);
        assertThat(humidity1).isNotEqualTo(humidity2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HumidityDTO.class);
        HumidityDTO humidityDTO1 = new HumidityDTO();
        humidityDTO1.setId(1L);
        HumidityDTO humidityDTO2 = new HumidityDTO();
        assertThat(humidityDTO1).isNotEqualTo(humidityDTO2);
        humidityDTO2.setId(humidityDTO1.getId());
        assertThat(humidityDTO1).isEqualTo(humidityDTO2);
        humidityDTO2.setId(2L);
        assertThat(humidityDTO1).isNotEqualTo(humidityDTO2);
        humidityDTO1.setId(null);
        assertThat(humidityDTO1).isNotEqualTo(humidityDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(humidityMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(humidityMapper.fromId(null)).isNull();
    }
}
