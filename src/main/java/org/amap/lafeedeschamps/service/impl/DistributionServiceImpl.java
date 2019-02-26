package org.amap.lafeedeschamps.service.impl;

import org.amap.lafeedeschamps.domain.Distribution;
import org.amap.lafeedeschamps.repository.DistributionRepository;
import org.amap.lafeedeschamps.service.DistributionService;
import org.amap.lafeedeschamps.service.dto.DistributionDTO;
import org.amap.lafeedeschamps.service.mapper.DistributionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Service Implementation for managing Distribution.
 */
@Service
@Transactional
public class DistributionServiceImpl implements DistributionService {

    private final Logger log = LoggerFactory.getLogger(DistributionServiceImpl.class);

    private final DistributionRepository distributionRepository;

    private final DistributionMapper distributionMapper;

    public DistributionServiceImpl(DistributionRepository distributionRepository, DistributionMapper distributionMapper) {
        this.distributionRepository = distributionRepository;
        this.distributionMapper = distributionMapper;
    }

    /**
     * Save a distribution.
     *
     * @param distributionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DistributionDTO save(DistributionDTO distributionDTO) {
        log.debug("Request to save Distribution : {}", distributionDTO);
        Distribution distribution = distributionMapper.toEntity(distributionDTO);
        distribution = distributionRepository.save(distribution);
        return distributionMapper.toDto(distribution);
    }

    /**
     * Get all the distributions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DistributionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Distributions");
        return distributionRepository.findAll(pageable)
            .map(distributionMapper::toDto);
    }

    /**
     * Get all the Distribution with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<DistributionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return distributionRepository.findAllWithEagerRelationships(pageable).map(distributionMapper::toDto);
    }


    /**
     * Get one distribution by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DistributionDTO> findOne(Long id) {
        log.debug("Request to get Distribution : {}", id);
        return distributionRepository.findOneWithEagerRelationships(id)
            .map(distributionMapper::toDto);
    }

    /**
     * Delete the distribution by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Distribution : {}", id);
        distributionRepository.deleteById(id);
    }

    @Override
    public Page<DistributionDTO> findByDates(Instant fromDate, Instant toDate, Pageable pageable) {
        return distributionRepository.findAllByStartDateBetweenOrderByStartDate(fromDate, toDate, pageable)
            .map(distributionMapper::toDto);
    }

    @Override
    public Page<DistributionDTO> findByDatesAndPlaceId(Instant fromDate, Instant toDate, Long placeId, Pageable pageable) {
        return distributionRepository.findAllByStartDateBetweenAndPlaceIdOrderByStartDate(fromDate, toDate, placeId, pageable)
            .map(distributionMapper::toDto);
    }


}
