package org.amap.lafeedeschamps.web.rest;

import org.amap.lafeedeschamps.BetteraveApp;

import org.amap.lafeedeschamps.domain.Distribution;
import org.amap.lafeedeschamps.repository.DistributionRepository;
import org.amap.lafeedeschamps.service.DistributionService;
import org.amap.lafeedeschamps.service.UserService;
import org.amap.lafeedeschamps.service.dto.DistributionDTO;
import org.amap.lafeedeschamps.service.mapper.DistributionMapper;
import org.amap.lafeedeschamps.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


import static org.amap.lafeedeschamps.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.amap.lafeedeschamps.domain.enumeration.Type;
/**
 * Test class for the DistributionResource REST controller.
 *
 * @see DistributionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BetteraveApp.class)
public class DistributionResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_MIN_USERS = 1;
    private static final Integer UPDATED_MIN_USERS = 2;

    private static final Type DEFAULT_TYPE = Type.DISTRIBUTION;
    private static final Type UPDATED_TYPE = Type.WORKSHOP;

    @Autowired
    private DistributionRepository distributionRepository;

    @Mock
    private DistributionRepository distributionRepositoryMock;

    @Autowired
    private DistributionMapper distributionMapper;

    @Mock
    private DistributionService distributionServiceMock;

    @Mock
    private UserService userServiceMock;

    @Autowired
    private DistributionService distributionService;

    @Autowired
    private UserService userService;

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

    private MockMvc restDistributionMockMvc;

    private Distribution distribution;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DistributionResource distributionResource = new DistributionResource(distributionService, userService);
        this.restDistributionMockMvc = MockMvcBuilders.standaloneSetup(distributionResource)
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
    public static Distribution createEntity(EntityManager em) {
        Distribution distribution = new Distribution()
            .date(DEFAULT_DATE)
            .text(DEFAULT_TEXT)
            .endDate(DEFAULT_END_DATE)
            .startDate(DEFAULT_START_DATE)
            .minUsers(DEFAULT_MIN_USERS)
            .type(DEFAULT_TYPE);
        return distribution;
    }

    @Before
    public void initTest() {
        distribution = createEntity(em);
    }

    @Test
    @Transactional
    public void createDistribution() throws Exception {
        int databaseSizeBeforeCreate = distributionRepository.findAll().size();

        // Create the Distribution
        DistributionDTO distributionDTO = distributionMapper.toDto(distribution);
        restDistributionMockMvc.perform(post("/api/distributions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distributionDTO)))
            .andExpect(status().isCreated());

        // Validate the Distribution in the database
        List<Distribution> distributionList = distributionRepository.findAll();
        assertThat(distributionList).hasSize(databaseSizeBeforeCreate + 1);
        Distribution testDistribution = distributionList.get(distributionList.size() - 1);
        assertThat(testDistribution.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDistribution.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testDistribution.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testDistribution.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testDistribution.getMinUsers()).isEqualTo(DEFAULT_MIN_USERS);
        assertThat(testDistribution.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createDistributionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = distributionRepository.findAll().size();

        // Create the Distribution with an existing ID
        distribution.setId(1L);
        DistributionDTO distributionDTO = distributionMapper.toDto(distribution);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDistributionMockMvc.perform(post("/api/distributions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distributionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Distribution in the database
        List<Distribution> distributionList = distributionRepository.findAll();
        assertThat(distributionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDistributions() throws Exception {
        // Initialize the database
        distributionRepository.saveAndFlush(distribution);

        // Get all the distributionList
        restDistributionMockMvc.perform(get("/api/distributions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(distribution.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].minUsers").value(hasItem(DEFAULT_MIN_USERS)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @SuppressWarnings({"unchecked"})
    public void getAllDistributionsWithEagerRelationshipsIsEnabled() throws Exception {
        DistributionResource distributionResource = new DistributionResource(distributionServiceMock, userServiceMock);
        when(distributionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restDistributionMockMvc = MockMvcBuilders.standaloneSetup(distributionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDistributionMockMvc.perform(get("/api/distributions?eagerload=true"))
        .andExpect(status().isOk());

        verify(distributionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllDistributionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        DistributionResource distributionResource = new DistributionResource(distributionServiceMock, userServiceMock);
            when(distributionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restDistributionMockMvc = MockMvcBuilders.standaloneSetup(distributionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDistributionMockMvc.perform(get("/api/distributions?eagerload=true"))
        .andExpect(status().isOk());

            verify(distributionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getDistribution() throws Exception {
        // Initialize the database
        distributionRepository.saveAndFlush(distribution);

        // Get the distribution
        restDistributionMockMvc.perform(get("/api/distributions/{id}", distribution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(distribution.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.minUsers").value(DEFAULT_MIN_USERS))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDistribution() throws Exception {
        // Get the distribution
        restDistributionMockMvc.perform(get("/api/distributions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDistribution() throws Exception {
        // Initialize the database
        distributionRepository.saveAndFlush(distribution);

        int databaseSizeBeforeUpdate = distributionRepository.findAll().size();

        // Update the distribution
        Distribution updatedDistribution = distributionRepository.findById(distribution.getId()).get();
        // Disconnect from session so that the updates on updatedDistribution are not directly saved in db
        em.detach(updatedDistribution);
        updatedDistribution
            .date(UPDATED_DATE)
            .text(UPDATED_TEXT)
            .endDate(UPDATED_END_DATE)
            .startDate(UPDATED_START_DATE)
            .minUsers(UPDATED_MIN_USERS)
            .type(UPDATED_TYPE);
        DistributionDTO distributionDTO = distributionMapper.toDto(updatedDistribution);

        restDistributionMockMvc.perform(put("/api/distributions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distributionDTO)))
            .andExpect(status().isOk());

        // Validate the Distribution in the database
        List<Distribution> distributionList = distributionRepository.findAll();
        assertThat(distributionList).hasSize(databaseSizeBeforeUpdate);
        Distribution testDistribution = distributionList.get(distributionList.size() - 1);
        assertThat(testDistribution.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDistribution.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testDistribution.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testDistribution.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDistribution.getMinUsers()).isEqualTo(UPDATED_MIN_USERS);
        assertThat(testDistribution.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingDistribution() throws Exception {
        int databaseSizeBeforeUpdate = distributionRepository.findAll().size();

        // Create the Distribution
        DistributionDTO distributionDTO = distributionMapper.toDto(distribution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDistributionMockMvc.perform(put("/api/distributions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distributionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Distribution in the database
        List<Distribution> distributionList = distributionRepository.findAll();
        assertThat(distributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDistribution() throws Exception {
        // Initialize the database
        distributionRepository.saveAndFlush(distribution);

        int databaseSizeBeforeDelete = distributionRepository.findAll().size();

        // Delete the distribution
        restDistributionMockMvc.perform(delete("/api/distributions/{id}", distribution.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Distribution> distributionList = distributionRepository.findAll();
        assertThat(distributionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Distribution.class);
        Distribution distribution1 = new Distribution();
        distribution1.setId(1L);
        Distribution distribution2 = new Distribution();
        distribution2.setId(distribution1.getId());
        assertThat(distribution1).isEqualTo(distribution2);
        distribution2.setId(2L);
        assertThat(distribution1).isNotEqualTo(distribution2);
        distribution1.setId(null);
        assertThat(distribution1).isNotEqualTo(distribution2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DistributionDTO.class);
        DistributionDTO distributionDTO1 = new DistributionDTO();
        distributionDTO1.setId(1L);
        DistributionDTO distributionDTO2 = new DistributionDTO();
        assertThat(distributionDTO1).isNotEqualTo(distributionDTO2);
        distributionDTO2.setId(distributionDTO1.getId());
        assertThat(distributionDTO1).isEqualTo(distributionDTO2);
        distributionDTO2.setId(2L);
        assertThat(distributionDTO1).isNotEqualTo(distributionDTO2);
        distributionDTO1.setId(null);
        assertThat(distributionDTO1).isNotEqualTo(distributionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(distributionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(distributionMapper.fromId(null)).isNull();
    }
}
