package com.robmelfi.rcraspi.web.rest;

import com.robmelfi.rcraspi.RcraspiApp;

import com.robmelfi.rcraspi.domain.Netatmo;
import com.robmelfi.rcraspi.repository.NetatmoRepository;
import com.robmelfi.rcraspi.service.NetatmoService;
import com.robmelfi.rcraspi.service.dto.NetatmoDTO;
import com.robmelfi.rcraspi.service.mapper.NetatmoMapper;
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
 * Test class for the NetatmoResource REST controller.
 *
 * @see NetatmoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RcraspiApp.class)
public class NetatmoResourceIntTest {

    private static final String DEFAULT_CLIENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_SECRET = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_SECRET = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "s?@s,.0";
    private static final String UPDATED_EMAIL = "^@Gw.f|";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private NetatmoRepository netatmoRepository;

    @Autowired
    private NetatmoMapper netatmoMapper;

    @Autowired
    private NetatmoService netatmoService;

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

    private MockMvc restNetatmoMockMvc;

    private Netatmo netatmo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NetatmoResource netatmoResource = new NetatmoResource(netatmoService);
        this.restNetatmoMockMvc = MockMvcBuilders.standaloneSetup(netatmoResource)
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
    public static Netatmo createEntity(EntityManager em) {
        Netatmo netatmo = new Netatmo()
            .clientId(DEFAULT_CLIENT_ID)
            .clientSecret(DEFAULT_CLIENT_SECRET)
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD)
            .enabled(DEFAULT_ENABLED);
        return netatmo;
    }

    @Before
    public void initTest() {
        netatmo = createEntity(em);
    }

    @Test
    @Transactional
    public void createNetatmo() throws Exception {
        int databaseSizeBeforeCreate = netatmoRepository.findAll().size();

        // Create the Netatmo
        NetatmoDTO netatmoDTO = netatmoMapper.toDto(netatmo);
        restNetatmoMockMvc.perform(post("/api/netatmos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(netatmoDTO)))
            .andExpect(status().isCreated());

        // Validate the Netatmo in the database
        List<Netatmo> netatmoList = netatmoRepository.findAll();
        assertThat(netatmoList).hasSize(databaseSizeBeforeCreate + 1);
        Netatmo testNetatmo = netatmoList.get(netatmoList.size() - 1);
        assertThat(testNetatmo.getClientId()).isEqualTo(DEFAULT_CLIENT_ID);
        assertThat(testNetatmo.getClientSecret()).isEqualTo(DEFAULT_CLIENT_SECRET);
        assertThat(testNetatmo.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testNetatmo.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testNetatmo.isEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    public void createNetatmoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = netatmoRepository.findAll().size();

        // Create the Netatmo with an existing ID
        netatmo.setId(1L);
        NetatmoDTO netatmoDTO = netatmoMapper.toDto(netatmo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNetatmoMockMvc.perform(post("/api/netatmos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(netatmoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Netatmo in the database
        List<Netatmo> netatmoList = netatmoRepository.findAll();
        assertThat(netatmoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkClientIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = netatmoRepository.findAll().size();
        // set the field null
        netatmo.setClientId(null);

        // Create the Netatmo, which fails.
        NetatmoDTO netatmoDTO = netatmoMapper.toDto(netatmo);

        restNetatmoMockMvc.perform(post("/api/netatmos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(netatmoDTO)))
            .andExpect(status().isBadRequest());

        List<Netatmo> netatmoList = netatmoRepository.findAll();
        assertThat(netatmoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClientSecretIsRequired() throws Exception {
        int databaseSizeBeforeTest = netatmoRepository.findAll().size();
        // set the field null
        netatmo.setClientSecret(null);

        // Create the Netatmo, which fails.
        NetatmoDTO netatmoDTO = netatmoMapper.toDto(netatmo);

        restNetatmoMockMvc.perform(post("/api/netatmos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(netatmoDTO)))
            .andExpect(status().isBadRequest());

        List<Netatmo> netatmoList = netatmoRepository.findAll();
        assertThat(netatmoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = netatmoRepository.findAll().size();
        // set the field null
        netatmo.setEmail(null);

        // Create the Netatmo, which fails.
        NetatmoDTO netatmoDTO = netatmoMapper.toDto(netatmo);

        restNetatmoMockMvc.perform(post("/api/netatmos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(netatmoDTO)))
            .andExpect(status().isBadRequest());

        List<Netatmo> netatmoList = netatmoRepository.findAll();
        assertThat(netatmoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = netatmoRepository.findAll().size();
        // set the field null
        netatmo.setPassword(null);

        // Create the Netatmo, which fails.
        NetatmoDTO netatmoDTO = netatmoMapper.toDto(netatmo);

        restNetatmoMockMvc.perform(post("/api/netatmos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(netatmoDTO)))
            .andExpect(status().isBadRequest());

        List<Netatmo> netatmoList = netatmoRepository.findAll();
        assertThat(netatmoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNetatmos() throws Exception {
        // Initialize the database
        netatmoRepository.saveAndFlush(netatmo);

        // Get all the netatmoList
        restNetatmoMockMvc.perform(get("/api/netatmos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(netatmo.getId().intValue())))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID.toString())))
            .andExpect(jsonPath("$.[*].clientSecret").value(hasItem(DEFAULT_CLIENT_SECRET.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getNetatmo() throws Exception {
        // Initialize the database
        netatmoRepository.saveAndFlush(netatmo);

        // Get the netatmo
        restNetatmoMockMvc.perform(get("/api/netatmos/{id}", netatmo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(netatmo.getId().intValue()))
            .andExpect(jsonPath("$.clientId").value(DEFAULT_CLIENT_ID.toString()))
            .andExpect(jsonPath("$.clientSecret").value(DEFAULT_CLIENT_SECRET.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNetatmo() throws Exception {
        // Get the netatmo
        restNetatmoMockMvc.perform(get("/api/netatmos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNetatmo() throws Exception {
        // Initialize the database
        netatmoRepository.saveAndFlush(netatmo);

        int databaseSizeBeforeUpdate = netatmoRepository.findAll().size();

        // Update the netatmo
        Netatmo updatedNetatmo = netatmoRepository.findById(netatmo.getId()).get();
        // Disconnect from session so that the updates on updatedNetatmo are not directly saved in db
        em.detach(updatedNetatmo);
        updatedNetatmo
            .clientId(UPDATED_CLIENT_ID)
            .clientSecret(UPDATED_CLIENT_SECRET)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .enabled(UPDATED_ENABLED);
        NetatmoDTO netatmoDTO = netatmoMapper.toDto(updatedNetatmo);

        restNetatmoMockMvc.perform(put("/api/netatmos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(netatmoDTO)))
            .andExpect(status().isOk());

        // Validate the Netatmo in the database
        List<Netatmo> netatmoList = netatmoRepository.findAll();
        assertThat(netatmoList).hasSize(databaseSizeBeforeUpdate);
        Netatmo testNetatmo = netatmoList.get(netatmoList.size() - 1);
        assertThat(testNetatmo.getClientId()).isEqualTo(UPDATED_CLIENT_ID);
        assertThat(testNetatmo.getClientSecret()).isEqualTo(UPDATED_CLIENT_SECRET);
        assertThat(testNetatmo.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testNetatmo.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testNetatmo.isEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingNetatmo() throws Exception {
        int databaseSizeBeforeUpdate = netatmoRepository.findAll().size();

        // Create the Netatmo
        NetatmoDTO netatmoDTO = netatmoMapper.toDto(netatmo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNetatmoMockMvc.perform(put("/api/netatmos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(netatmoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Netatmo in the database
        List<Netatmo> netatmoList = netatmoRepository.findAll();
        assertThat(netatmoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNetatmo() throws Exception {
        // Initialize the database
        netatmoRepository.saveAndFlush(netatmo);

        int databaseSizeBeforeDelete = netatmoRepository.findAll().size();

        // Get the netatmo
        restNetatmoMockMvc.perform(delete("/api/netatmos/{id}", netatmo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Netatmo> netatmoList = netatmoRepository.findAll();
        assertThat(netatmoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Netatmo.class);
        Netatmo netatmo1 = new Netatmo();
        netatmo1.setId(1L);
        Netatmo netatmo2 = new Netatmo();
        netatmo2.setId(netatmo1.getId());
        assertThat(netatmo1).isEqualTo(netatmo2);
        netatmo2.setId(2L);
        assertThat(netatmo1).isNotEqualTo(netatmo2);
        netatmo1.setId(null);
        assertThat(netatmo1).isNotEqualTo(netatmo2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NetatmoDTO.class);
        NetatmoDTO netatmoDTO1 = new NetatmoDTO();
        netatmoDTO1.setId(1L);
        NetatmoDTO netatmoDTO2 = new NetatmoDTO();
        assertThat(netatmoDTO1).isNotEqualTo(netatmoDTO2);
        netatmoDTO2.setId(netatmoDTO1.getId());
        assertThat(netatmoDTO1).isEqualTo(netatmoDTO2);
        netatmoDTO2.setId(2L);
        assertThat(netatmoDTO1).isNotEqualTo(netatmoDTO2);
        netatmoDTO1.setId(null);
        assertThat(netatmoDTO1).isNotEqualTo(netatmoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(netatmoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(netatmoMapper.fromId(null)).isNull();
    }
}
