package org.amap.lafeedeschamps.service;

import org.amap.lafeedeschamps.service.dto.DistributionPlaceDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing DistributionPlace.
 */
public interface DistributionPlaceService {

    /**
     * Save a distributionPlace.
     *
     * @param distributionPlaceDTO the entity to save
     * @return the persisted entity
     */
    DistributionPlaceDTO save(DistributionPlaceDTO distributionPlaceDTO);

    /**
     * Get all the distributionPlaces.
     *
     * @return the list of entities
     */
    List<DistributionPlaceDTO> findAll();


    /**
     * Get the "id" distributionPlace.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DistributionPlaceDTO> findOne(Long id);

    /**
     * Delete the "id" distributionPlace.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
