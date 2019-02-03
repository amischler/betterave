package org.amap.lafeedeschamps.repository;

import org.amap.lafeedeschamps.domain.DistributionPlace;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DistributionPlace entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DistributionPlaceRepository extends JpaRepository<DistributionPlace, Long> {

}
