package org.amap.lafeedeschamps.repository;

import org.amap.lafeedeschamps.domain.Comment;
import org.amap.lafeedeschamps.service.dto.CommentDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Comment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select comment from Comment comment where comment.user.login = ?#{principal.username}")
    List<Comment> findByUserIsCurrentUser();

    @Query("select comment from Comment comment left join fetch comment.user where comment.distribution.id =:id")
    List<Comment> findAllByDistributionIdWithEagerRelationships(@Param("id") Long distributionId);

}
