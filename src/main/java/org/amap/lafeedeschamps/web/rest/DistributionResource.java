package org.amap.lafeedeschamps.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import org.amap.lafeedeschamps.domain.User;
import org.amap.lafeedeschamps.service.DistributionService;
import org.amap.lafeedeschamps.service.UserService;
import org.amap.lafeedeschamps.service.dto.DistributionDTO;
import org.amap.lafeedeschamps.service.dto.UserDTO;
import org.amap.lafeedeschamps.web.rest.errors.BadRequestAlertException;
import org.amap.lafeedeschamps.web.rest.util.HeaderUtil;
import org.amap.lafeedeschamps.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Distribution.
 */
@RestController
@RequestMapping("/api")
public class DistributionResource {

    private final Logger log = LoggerFactory.getLogger(DistributionResource.class);

    private static final String ENTITY_NAME = "distribution";

    private final DistributionService distributionService;

    private final UserService userService;

    public DistributionResource(DistributionService distributionService, UserService userService) {
        this.distributionService = distributionService;
        this.userService = userService;
    }

    /**
     * POST  /distributions : Create a new distribution.
     *
     * @param distributionDTO the distributionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new distributionDTO, or with status 400 (Bad Request) if the distribution has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/distributions")
    public ResponseEntity<DistributionDTO> createDistribution(@RequestBody DistributionDTO distributionDTO) throws URISyntaxException {
        log.debug("REST request to save Distribution : {}", distributionDTO);
        if (distributionDTO.getId() != null) {
            throw new BadRequestAlertException("A new distribution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DistributionDTO result = distributionService.save(distributionDTO);
        return ResponseEntity.created(new URI("/api/distributions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /distributions : Updates an existing distribution.
     *
     * @param distributionDTO the distributionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated distributionDTO,
     * or with status 400 (Bad Request) if the distributionDTO is not valid,
     * or with status 500 (Internal Server Error) if the distributionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/distributions")
    public ResponseEntity<DistributionDTO> updateDistribution(@RequestBody DistributionDTO distributionDTO) throws URISyntaxException {
        log.debug("REST request to update Distribution : {}", distributionDTO);
        if (distributionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DistributionDTO result = distributionService.save(distributionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, distributionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /distributions : get all the distributions.
     *
     * @param pageable  the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of distributions in body
     */
    @GetMapping("/distributions")
    public ResponseEntity<List<DistributionDTO>> getAllDistributions(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Distributions");
        Page<DistributionDTO> page;
        if (eagerload) {
            page = distributionService.findAllWithEagerRelationships(pageable);
        } else {
            page = distributionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/distributions?eagerload=%b", eagerload));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /distributions/:id : get the "id" distribution.
     *
     * @param id the id of the distributionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the distributionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/distributions/{id}")
    public ResponseEntity<DistributionDTO> getDistribution(@PathVariable Long id) {
        log.debug("REST request to get Distribution : {}", id);
        Optional<DistributionDTO> distributionDTO = distributionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(distributionDTO);
    }

    /**
     * DELETE  /distributions/:id : delete the "id" distribution.
     *
     * @param id the id of the distributionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/distributions/{id}")
    public ResponseEntity<Void> deleteDistribution(@PathVariable Long id) {
        log.debug("REST request to delete Distribution : {}", id);
        distributionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET /distributions/:id/subscribe : subscribes to the "id" distribution.
     *
     * @param id the id of the distribution to subscribe to
     * @return the ResponseEntity with status 200 (OK)
     */
    @GetMapping("/distributions/{id}/subscribe")
    public ResponseEntity<DistributionDTO> subscribeToDistribution(@PathVariable Long id) {
        log.debug("REST request to subscribe to Distribution : {}", id);
        Optional<DistributionDTO> optionalDistribution = distributionService.findOne(id);
        if (optionalDistribution.isPresent()) {
            DistributionDTO distribution = optionalDistribution.get();
            Optional<User> user = userService.getUserWithAuthorities();
            if (user.isPresent()) {
                distribution.getUsers().add(new UserDTO(user.get()));
            }
            distribution = distributionService.save(distribution);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert("Merci pour votre inscription à la distribution !",
                    distribution.getId().toString()))
                .body(distribution);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    /**
     * DELETEu /distributions/:id/subscribe : subscribes to the "id" distribution.
     *
     * @param id the id of the distribution to subscribe to
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/distributions/{id}/subscribe")
    public ResponseEntity<DistributionDTO> unsubscribeFromDistribution(@PathVariable Long id) {
        log.debug("REST request to unsubscribe from Distribution : {}", id);
        Optional<DistributionDTO> optionalDistribution = distributionService.findOne(id);
        if (optionalDistribution.isPresent()) {
            DistributionDTO distribution = optionalDistribution.get();
            Optional<User> user = userService.getUserWithAuthorities();
            if (user.isPresent()) {
                Optional<UserDTO> toRemove = distribution.getUsers().stream().filter(u -> u.getLogin().equals(user.get().getLogin())).findFirst();
                if (toRemove.isPresent()) {
                    distribution.getUsers().remove(toRemove.get());
                }
            }
            distribution = distributionService.save(distribution);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert("Votre désinscription a bien été enregistrée",
                    distribution.getId().toString()))
                .body(distribution);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
