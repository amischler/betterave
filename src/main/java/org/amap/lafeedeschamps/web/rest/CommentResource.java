package org.amap.lafeedeschamps.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import org.amap.lafeedeschamps.domain.User;
import org.amap.lafeedeschamps.repository.DistributionRepository;
import org.amap.lafeedeschamps.service.CommentService;
import org.amap.lafeedeschamps.service.MailService;
import org.amap.lafeedeschamps.service.UserService;
import org.amap.lafeedeschamps.service.dto.CommentDTO;
import org.amap.lafeedeschamps.service.mapper.CommentMapper;
import org.amap.lafeedeschamps.web.rest.errors.BadRequestAlertException;
import org.amap.lafeedeschamps.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Comment.
 */
@RestController
@RequestMapping("/api")
public class CommentResource {

    private final Logger log = LoggerFactory.getLogger(CommentResource.class);

    private static final String ENTITY_NAME = "comment";

    private final CommentService commentService;

    private final UserService userService;

    private final DistributionRepository distributionRepository;

    private final MailService mailService;

    private final CommentMapper commentMapper;

    public CommentResource(CommentService commentService, UserService userService, DistributionRepository distributionRepository, MailService mailService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.userService = userService;
        this.distributionRepository = distributionRepository;
        this.mailService = mailService;
        this.commentMapper = commentMapper;
    }

    /**
     * POST  /comments : Create a new comment.
     *
     * @param commentDTO the commentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commentDTO, or with status 400 (Bad Request) if the comment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/comments")
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) throws URISyntaxException {
        log.debug("REST request to save Comment : {}", commentDTO);
        if (commentDTO.getId() != null) {
            throw new BadRequestAlertException("A new comment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Optional<User> user = userService.getUserWithAuthorities();
        user.ifPresent(u -> commentDTO.setUserId(u.getId()));
        CommentDTO result = commentService.save(commentDTO);
        if (commentDTO.getDistributionId() != null) {
            distributionRepository.findOneWithEagerRelationships(commentDTO.getDistributionId()).ifPresent(distribution ->
                mailService.sendCommentEmail(user.get(), commentMapper.toEntity(commentDTO), distribution));
        }
        return ResponseEntity.created(new URI("/api/comments/" + result.getId()))
            .body(result);
    }

    /**
     * PUT  /comments : Updates an existing comment.
     *
     * @param commentDTO the commentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commentDTO,
     * or with status 400 (Bad Request) if the commentDTO is not valid,
     * or with status 500 (Internal Server Error) if the commentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/comments")
    public ResponseEntity<CommentDTO> updateComment(@RequestBody CommentDTO commentDTO) throws URISyntaxException {
        log.debug("REST request to update Comment : {}", commentDTO);
        if (commentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CommentDTO result = commentService.save(commentDTO);
        return ResponseEntity.ok()
            .body(result);
    }

    /**
     * GET  /comments : get all the comments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of comments in body
     */
    @GetMapping("/comments")
    public List<CommentDTO> getAllComments() {
        log.debug("REST request to get all Comments");
        return commentService.findAll();
    }

    /**
     * GET  /comments/:id : get the "id" comment.
     *
     * @param id the id of the commentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable Long id) {
        log.debug("REST request to get Comment : {}", id);
        Optional<CommentDTO> commentDTO = commentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commentDTO);
    }

    /**
     * GET  /comments?distributionId=1 : get the comments for the given distributionId
     *
     * @param distributionId the id of the commentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commentDTO, or with status 404 (Not Found)
     */
    @GetMapping(value = "/comments", params = {"distributionId"})
    public List<CommentDTO> getCommentsByDistributionId(@RequestParam Long distributionId) {
        log.debug("REST request to get Comments by distributionId : {}", distributionId);
        return commentService.findByDistributionId(distributionId);
    }

    /**
     * DELETE  /comments/:id : delete the "id" comment.
     *
     * @param id the id of the commentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        log.debug("REST request to delete Comment : {}", id);
        commentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
