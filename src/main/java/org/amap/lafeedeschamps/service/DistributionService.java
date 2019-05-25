package org.amap.lafeedeschamps.service;

import org.amap.lafeedeschamps.service.dto.DistributionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Distribution.
 */
public interface DistributionService {

    /**
     * Save a distribution.
     *
     * @param distributionDTO the entity to save
     * @return the persisted entity
     */
    DistributionDTO save(DistributionDTO distributionDTO);

    /**
     * Get all the distributions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DistributionDTO> findAll(Pageable pageable);

    /**
     * Get all the Distribution with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<DistributionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" distribution.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DistributionDTO> findOne(Long id);

    /**
     * Delete the "id" distribution.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    Page<DistributionDTO> findByDates(Instant fromDate, Instant toDate, Pageable pageable);

    List<DistributionDTO> findByDates(Instant fromDate, Instant toDate);

    Page<DistributionDTO> findByDatesAndPlaceId(Instant fromDate, Instant toDate, Long placeId, Pageable pageable);

    List<DistributionDTO> findByUsers_Id(Long userId);

}
