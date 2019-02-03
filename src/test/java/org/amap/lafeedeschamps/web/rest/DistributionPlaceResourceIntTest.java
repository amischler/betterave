package org.amap.lafeedeschamps.web.rest;

import org.amap.lafeedeschamps.BetteraveApp;

import org.amap.lafeedeschamps.domain.DistributionPlace;
import org.amap.lafeedeschamps.repository.DistributionPlaceRepository;
import org.amap.lafeedeschamps.service.DistributionPlaceService;
import org.amap.lafeedeschamps.service.dto.DistributionPlaceDTO;
import org.amap.lafeedeschamps.service.mapper.DistributionPlaceMapper;
import org.amap.lafeedeschamps.web.rest.errors.ExceptionTranslator;

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


import static org.amap.lafeedeschamps.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DistributionPlaceResource REST controller.
 *
 * @see DistributionPlaceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BetteraveApp.class)
public class DistributionPlaceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private DistributionPlaceRepository distributionPlaceRepository;

    @Autowired
    private DistributionPlaceMapper distributionPlaceMapper;

    @Autowired
    private DistributionPlaceService distributionPlaceService;

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

    private MockMvc restDistributionPlaceMockMvc;

    private DistributionPlace distributionPlace;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DistributionPlaceResource distributionPlaceResource = new DistributionPlaceResource(distributionPlaceService);
        this.restDistributionPlaceMockMvc = MockMvcBuilders.standaloneSetup(distributionPlaceResource)
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
    public static DistributionPlace createEntity(EntityManager em) {
        DistributionPlace distributionPlace = new DistributionPlace()
            .name(DEFAULT_NAME);
        return distributionPlace;
    }

    @Before
    public void initTest() {
        distributionPlace = createEntity(em);
    }

    @Test
    @Transactional
    public void createDistributionPlace() throws Exception {
        int databaseSizeBeforeCreate = distributionPlaceRepository.findAll().size();

        // Create the DistributionPlace
        DistributionPlaceDTO distributionPlaceDTO = distributionPlaceMapper.toDto(distributionPlace);
        restDistributionPlaceMockMvc.perform(post("/api/distribution-places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distributionPlaceDTO)))
            .andExpect(status().isCreated());

        // Validate the DistributionPlace in the database
        List<DistributionPlace> distributionPlaceList = distributionPlaceRepository.findAll();
        assertThat(distributionPlaceList).hasSize(databaseSizeBeforeCreate + 1);
        DistributionPlace testDistributionPlace = distributionPlaceList.get(distributionPlaceList.size() - 1);
        assertThat(testDistributionPlace.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createDistributionPlaceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = distributionPlaceRepository.findAll().size();

        // Create the DistributionPlace with an existing ID
        distributionPlace.setId(1L);
        DistributionPlaceDTO distributionPlaceDTO = distributionPlaceMapper.toDto(distributionPlace);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDistributionPlaceMockMvc.perform(post("/api/distribution-places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distributionPlaceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DistributionPlace in the database
        List<DistributionPlace> distributionPlaceList = distributionPlaceRepository.findAll();
        assertThat(distributionPlaceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDistributionPlaces() throws Exception {
        // Initialize the database
        distributionPlaceRepository.saveAndFlush(distributionPlace);

        // Get all the distributionPlaceList
        restDistributionPlaceMockMvc.perform(get("/api/distribution-places?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(distributionPlace.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getDistributionPlace() throws Exception {
        // Initialize the database
        distributionPlaceRepository.saveAndFlush(distributionPlace);

        // Get the distributionPlace
        restDistributionPlaceMockMvc.perform(get("/api/distribution-places/{id}", distributionPlace.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(distributionPlace.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDistributionPlace() throws Exception {
        // Get the distributionPlace
        restDistributionPlaceMockMvc.perform(get("/api/distribution-places/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDistributionPlace() throws Exception {
        // Initialize the database
        distributionPlaceRepository.saveAndFlush(distributionPlace);

        int databaseSizeBeforeUpdate = distributionPlaceRepository.findAll().size();

        // Update the distributionPlace
        DistributionPlace updatedDistributionPlace = distributionPlaceRepository.findById(distributionPlace.getId()).get();
        // Disconnect from session so that the updates on updatedDistributionPlace are not directly saved in db
        em.detach(updatedDistributionPlace);
        updatedDistributionPlace
            .name(UPDATED_NAME);
        DistributionPlaceDTO distributionPlaceDTO = distributionPlaceMapper.toDto(updatedDistributionPlace);

        restDistributionPlaceMockMvc.perform(put("/api/distribution-places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distributionPlaceDTO)))
            .andExpect(status().isOk());

        // Validate the DistributionPlace in the database
        List<DistributionPlace> distributionPlaceList = distributionPlaceRepository.findAll();
        assertThat(distributionPlaceList).hasSize(databaseSizeBeforeUpdate);
        DistributionPlace testDistributionPlace = distributionPlaceList.get(distributionPlaceList.size() - 1);
        assertThat(testDistributionPlace.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingDistributionPlace() throws Exception {
        int databaseSizeBeforeUpdate = distributionPlaceRepository.findAll().size();

        // Create the DistributionPlace
        DistributionPlaceDTO distributionPlaceDTO = distributionPlaceMapper.toDto(distributionPlace);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDistributionPlaceMockMvc.perform(put("/api/distribution-places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distributionPlaceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DistributionPlace in the database
        List<DistributionPlace> distributionPlaceList = distributionPlaceRepository.findAll();
        assertThat(distributionPlaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDistributionPlace() throws Exception {
        // Initialize the database
        distributionPlaceRepository.saveAndFlush(distributionPlace);

        int databaseSizeBeforeDelete = distributionPlaceRepository.findAll().size();

        // Delete the distributionPlace
        restDistributionPlaceMockMvc.perform(delete("/api/distribution-places/{id}", distributionPlace.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DistributionPlace> distributionPlaceList = distributionPlaceRepository.findAll();
        assertThat(distributionPlaceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DistributionPlace.class);
        DistributionPlace distributionPlace1 = new DistributionPlace();
        distributionPlace1.setId(1L);
        DistributionPlace distributionPlace2 = new DistributionPlace();
        distributionPlace2.setId(distributionPlace1.getId());
        assertThat(distributionPlace1).isEqualTo(distributionPlace2);
        distributionPlace2.setId(2L);
        assertThat(distributionPlace1).isNotEqualTo(distributionPlace2);
        distributionPlace1.setId(null);
        assertThat(distributionPlace1).isNotEqualTo(distributionPlace2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DistributionPlaceDTO.class);
        DistributionPlaceDTO distributionPlaceDTO1 = new DistributionPlaceDTO();
        distributionPlaceDTO1.setId(1L);
        DistributionPlaceDTO distributionPlaceDTO2 = new DistributionPlaceDTO();
        assertThat(distributionPlaceDTO1).isNotEqualTo(distributionPlaceDTO2);
        distributionPlaceDTO2.setId(distributionPlaceDTO1.getId());
        assertThat(distributionPlaceDTO1).isEqualTo(distributionPlaceDTO2);
        distributionPlaceDTO2.setId(2L);
        assertThat(distributionPlaceDTO1).isNotEqualTo(distributionPlaceDTO2);
        distributionPlaceDTO1.setId(null);
        assertThat(distributionPlaceDTO1).isNotEqualTo(distributionPlaceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(distributionPlaceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(distributionPlaceMapper.fromId(null)).isNull();
    }
}
