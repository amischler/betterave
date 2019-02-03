package org.amap.lafeedeschamps.service.mapper;

import org.amap.lafeedeschamps.domain.*;
import org.amap.lafeedeschamps.service.dto.DistributionPlaceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DistributionPlace and its DTO DistributionPlaceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DistributionPlaceMapper extends EntityMapper<DistributionPlaceDTO, DistributionPlace> {



    default DistributionPlace fromId(Long id) {
        if (id == null) {
            return null;
        }
        DistributionPlace distributionPlace = new DistributionPlace();
        distributionPlace.setId(id);
        return distributionPlace;
    }
}
