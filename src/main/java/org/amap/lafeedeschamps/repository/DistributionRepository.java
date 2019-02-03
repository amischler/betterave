package org.amap.lafeedeschamps.repository;

import org.amap.lafeedeschamps.domain.Distribution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Distribution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DistributionRepository extends JpaRepository<Distribution, Long> {

    @Query(value = "select distinct distribution from Distribution distribution left join fetch distribution.users",
        countQuery = "select count(distinct distribution) from Distribution distribution")
    Page<Distribution> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct distribution from Distribution distribution left join fetch distribution.users")
    List<Distribution> findAllWithEagerRelationships();

    @Query("select distribution from Distribution distribution left join fetch distribution.users where distribution.id =:id")
    Optional<Distribution> findOneWithEagerRelationships(@Param("id") Long id);

}
