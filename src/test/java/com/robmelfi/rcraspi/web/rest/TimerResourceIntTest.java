package com.robmelfi.rcraspi.web.rest;

import com.robmelfi.rcraspi.RcraspiApp;

import com.robmelfi.rcraspi.domain.Timer;
import com.robmelfi.rcraspi.repository.TimerRepository;
import com.robmelfi.rcraspi.service.TimerService;
import com.robmelfi.rcraspi.service.dto.TimerDTO;
import com.robmelfi.rcraspi.service.mapper.TimerMapper;
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

import com.robmelfi.rcraspi.domain.enumeration.Repeat;
/**
 * Test class for the TimerResource REST controller.
 *
 * @see TimerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RcraspiApp.class)
public class TimerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_STOP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_STOP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Repeat DEFAULT_REPEAT = Repeat.ONCE;
    private static final Repeat UPDATED_REPEAT = Repeat.DAY;

    @Autowired
    private TimerRepository timerRepository;

    @Autowired
    private TimerMapper timerMapper;

    @Autowired
    private TimerService timerService;

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

    private MockMvc restTimerMockMvc;

    private Timer timer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TimerResource timerResource = new TimerResource(timerService);
        this.restTimerMockMvc = MockMvcBuilders.standaloneSetup(timerResource)
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
    public static Timer createEntity(EntityManager em) {
        Timer timer = new Timer()
            .name(DEFAULT_NAME)
            .start(DEFAULT_START)
            .stop(DEFAULT_STOP)
            .repeat(DEFAULT_REPEAT);
        return timer;
    }

    @Before
    public void initTest() {
        timer = createEntity(em);
    }

    @Test
    @Transactional
    public void createTimer() throws Exception {
        int databaseSizeBeforeCreate = timerRepository.findAll().size();

        // Create the Timer
        TimerDTO timerDTO = timerMapper.toDto(timer);
        restTimerMockMvc.perform(post("/api/timers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timerDTO)))
            .andExpect(status().isCreated());

        // Validate the Timer in the database
        List<Timer> timerList = timerRepository.findAll();
        assertThat(timerList).hasSize(databaseSizeBeforeCreate + 1);
        Timer testTimer = timerList.get(timerList.size() - 1);
        assertThat(testTimer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTimer.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testTimer.getStop()).isEqualTo(DEFAULT_STOP);
        assertThat(testTimer.getRepeat()).isEqualTo(DEFAULT_REPEAT);
    }

    @Test
    @Transactional
    public void createTimerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = timerRepository.findAll().size();

        // Create the Timer with an existing ID
        timer.setId(1L);
        TimerDTO timerDTO = timerMapper.toDto(timer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimerMockMvc.perform(post("/api/timers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Timer in the database
        List<Timer> timerList = timerRepository.findAll();
        assertThat(timerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = timerRepository.findAll().size();
        // set the field null
        timer.setName(null);

        // Create the Timer, which fails.
        TimerDTO timerDTO = timerMapper.toDto(timer);

        restTimerMockMvc.perform(post("/api/timers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timerDTO)))
            .andExpect(status().isBadRequest());

        List<Timer> timerList = timerRepository.findAll();
        assertThat(timerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = timerRepository.findAll().size();
        // set the field null
        timer.setStart(null);

        // Create the Timer, which fails.
        TimerDTO timerDTO = timerMapper.toDto(timer);

        restTimerMockMvc.perform(post("/api/timers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timerDTO)))
            .andExpect(status().isBadRequest());

        List<Timer> timerList = timerRepository.findAll();
        assertThat(timerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStopIsRequired() throws Exception {
        int databaseSizeBeforeTest = timerRepository.findAll().size();
        // set the field null
        timer.setStop(null);

        // Create the Timer, which fails.
        TimerDTO timerDTO = timerMapper.toDto(timer);

        restTimerMockMvc.perform(post("/api/timers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timerDTO)))
            .andExpect(status().isBadRequest());

        List<Timer> timerList = timerRepository.findAll();
        assertThat(timerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTimers() throws Exception {
        // Initialize the database
        timerRepository.saveAndFlush(timer);

        // Get all the timerList
        restTimerMockMvc.perform(get("/api/timers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(sameInstant(DEFAULT_START))))
            .andExpect(jsonPath("$.[*].stop").value(hasItem(sameInstant(DEFAULT_STOP))))
            .andExpect(jsonPath("$.[*].repeat").value(hasItem(DEFAULT_REPEAT.toString())));
    }
    
    @Test
    @Transactional
    public void getTimer() throws Exception {
        // Initialize the database
        timerRepository.saveAndFlush(timer);

        // Get the timer
        restTimerMockMvc.perform(get("/api/timers/{id}", timer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(timer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.start").value(sameInstant(DEFAULT_START)))
            .andExpect(jsonPath("$.stop").value(sameInstant(DEFAULT_STOP)))
            .andExpect(jsonPath("$.repeat").value(DEFAULT_REPEAT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTimer() throws Exception {
        // Get the timer
        restTimerMockMvc.perform(get("/api/timers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTimer() throws Exception {
        // Initialize the database
        timerRepository.saveAndFlush(timer);

        int databaseSizeBeforeUpdate = timerRepository.findAll().size();

        // Update the timer
        Timer updatedTimer = timerRepository.findById(timer.getId()).get();
        // Disconnect from session so that the updates on updatedTimer are not directly saved in db
        em.detach(updatedTimer);
        updatedTimer
            .name(UPDATED_NAME)
            .start(UPDATED_START)
            .stop(UPDATED_STOP)
            .repeat(UPDATED_REPEAT);
        TimerDTO timerDTO = timerMapper.toDto(updatedTimer);

        restTimerMockMvc.perform(put("/api/timers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timerDTO)))
            .andExpect(status().isOk());

        // Validate the Timer in the database
        List<Timer> timerList = timerRepository.findAll();
        assertThat(timerList).hasSize(databaseSizeBeforeUpdate);
        Timer testTimer = timerList.get(timerList.size() - 1);
        assertThat(testTimer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTimer.getStart()).isEqualTo(UPDATED_START);
        assertThat(testTimer.getStop()).isEqualTo(UPDATED_STOP);
        assertThat(testTimer.getRepeat()).isEqualTo(UPDATED_REPEAT);
    }

    @Test
    @Transactional
    public void updateNonExistingTimer() throws Exception {
        int databaseSizeBeforeUpdate = timerRepository.findAll().size();

        // Create the Timer
        TimerDTO timerDTO = timerMapper.toDto(timer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimerMockMvc.perform(put("/api/timers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Timer in the database
        List<Timer> timerList = timerRepository.findAll();
        assertThat(timerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTimer() throws Exception {
        // Initialize the database
        timerRepository.saveAndFlush(timer);

        int databaseSizeBeforeDelete = timerRepository.findAll().size();

        // Get the timer
        restTimerMockMvc.perform(delete("/api/timers/{id}", timer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Timer> timerList = timerRepository.findAll();
        assertThat(timerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Timer.class);
        Timer timer1 = new Timer();
        timer1.setId(1L);
        Timer timer2 = new Timer();
        timer2.setId(timer1.getId());
        assertThat(timer1).isEqualTo(timer2);
        timer2.setId(2L);
        assertThat(timer1).isNotEqualTo(timer2);
        timer1.setId(null);
        assertThat(timer1).isNotEqualTo(timer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimerDTO.class);
        TimerDTO timerDTO1 = new TimerDTO();
        timerDTO1.setId(1L);
        TimerDTO timerDTO2 = new TimerDTO();
        assertThat(timerDTO1).isNotEqualTo(timerDTO2);
        timerDTO2.setId(timerDTO1.getId());
        assertThat(timerDTO1).isEqualTo(timerDTO2);
        timerDTO2.setId(2L);
        assertThat(timerDTO1).isNotEqualTo(timerDTO2);
        timerDTO1.setId(null);
        assertThat(timerDTO1).isNotEqualTo(timerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(timerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(timerMapper.fromId(null)).isNull();
    }
}
