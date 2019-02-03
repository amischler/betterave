package org.amap.lafeedeschamps.service.mapper;

import org.amap.lafeedeschamps.domain.*;
import org.amap.lafeedeschamps.service.dto.DistributionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Distribution and its DTO DistributionDTO.
 */
@Mapper(componentModel = "spring", uses = {DistributionPlaceMapper.class, UserMapper.class})
public interface DistributionMapper extends EntityMapper<DistributionDTO, Distribution> {

    @Mapping(source = "place.id", target = "placeId")
    @Mapping(source = "place.name", target = "placeName")
    DistributionDTO toDto(Distribution distribution);

    @Mapping(target = "comments", ignore = true)
    @Mapping(source = "placeId", target = "place")
    Distribution toEntity(DistributionDTO distributionDTO);

    default Distribution fromId(Long id) {
        if (id == null) {
            return null;
        }
        Distribution distribution = new Distribution();
        distribution.setId(id);
        return distribution;
    }
}
