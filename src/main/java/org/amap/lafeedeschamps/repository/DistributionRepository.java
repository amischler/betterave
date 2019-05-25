package org.amap.lafeedeschamps.repository;

import org.amap.lafeedeschamps.domain.Distribution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
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

    Page<Distribution> findAllByStartDateBetweenAndPlaceIdOrderByStartDate(Instant fromDate, Instant toDate, Long placeId, Pageable pageable);

    Page<Distribution> findAllByStartDateBetweenOrderByStartDate(Instant fromDate, Instant toDate, Pageable pageable);

    List<Distribution> findAllByStartDateBetweenOrderByStartDate(Instant fromDate, Instant toDate);

    List<Distribution> findByUsers_IdOrderByStartDateDesc(@Param("userId") Long userId);

}
