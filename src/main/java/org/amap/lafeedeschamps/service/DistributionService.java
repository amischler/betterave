package org.amap.lafeedeschamps.service;

import org.amap.lafeedeschamps.service.dto.DistributionDTO;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
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

    Page<DistributionDTO> findByDates(LocalDate fromDate, LocalDate toDate, Pageable pageable);
}
