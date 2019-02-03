package org.amap.lafeedeschamps.service.impl;

import org.amap.lafeedeschamps.service.DistributionPlaceService;
import org.amap.lafeedeschamps.domain.DistributionPlace;
import org.amap.lafeedeschamps.repository.DistributionPlaceRepository;
import org.amap.lafeedeschamps.service.dto.DistributionPlaceDTO;
import org.amap.lafeedeschamps.service.mapper.DistributionPlaceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing DistributionPlace.
 */
@Service
@Transactional
public class DistributionPlaceServiceImpl implements DistributionPlaceService {

    private final Logger log = LoggerFactory.getLogger(DistributionPlaceServiceImpl.class);

    private final DistributionPlaceRepository distributionPlaceRepository;

    private final DistributionPlaceMapper distributionPlaceMapper;

    public DistributionPlaceServiceImpl(DistributionPlaceRepository distributionPlaceRepository, DistributionPlaceMapper distributionPlaceMapper) {
        this.distributionPlaceRepository = distributionPlaceRepository;
        this.distributionPlaceMapper = distributionPlaceMapper;
    }

    /**
     * Save a distributionPlace.
     *
     * @param distributionPlaceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DistributionPlaceDTO save(DistributionPlaceDTO distributionPlaceDTO) {
        log.debug("Request to save DistributionPlace : {}", distributionPlaceDTO);
        DistributionPlace distributionPlace = distributionPlaceMapper.toEntity(distributionPlaceDTO);
        distributionPlace = distributionPlaceRepository.save(distributionPlace);
        return distributionPlaceMapper.toDto(distributionPlace);
    }

    /**
     * Get all the distributionPlaces.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<DistributionPlaceDTO> findAll() {
        log.debug("Request to get all DistributionPlaces");
        return distributionPlaceRepository.findAll().stream()
            .map(distributionPlaceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one distributionPlace by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DistributionPlaceDTO> findOne(Long id) {
        log.debug("Request to get DistributionPlace : {}", id);
        return distributionPlaceRepository.findById(id)
            .map(distributionPlaceMapper::toDto);
    }

    /**
     * Delete the distributionPlace by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DistributionPlace : {}", id);        distributionPlaceRepository.deleteById(id);
    }
}
