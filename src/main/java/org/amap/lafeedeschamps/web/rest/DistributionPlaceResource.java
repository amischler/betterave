package org.amap.lafeedeschamps.web.rest;
import org.amap.lafeedeschamps.service.DistributionPlaceService;
import org.amap.lafeedeschamps.web.rest.errors.BadRequestAlertException;
import org.amap.lafeedeschamps.web.rest.util.HeaderUtil;
import org.amap.lafeedeschamps.service.dto.DistributionPlaceDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DistributionPlace.
 */
@RestController
@RequestMapping("/api")
public class DistributionPlaceResource {

    private final Logger log = LoggerFactory.getLogger(DistributionPlaceResource.class);

    private static final String ENTITY_NAME = "distributionPlace";

    private final DistributionPlaceService distributionPlaceService;

    public DistributionPlaceResource(DistributionPlaceService distributionPlaceService) {
        this.distributionPlaceService = distributionPlaceService;
    }

    /**
     * POST  /distribution-places : Create a new distributionPlace.
     *
     * @param distributionPlaceDTO the distributionPlaceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new distributionPlaceDTO, or with status 400 (Bad Request) if the distributionPlace has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/distribution-places")
    public ResponseEntity<DistributionPlaceDTO> createDistributionPlace(@RequestBody DistributionPlaceDTO distributionPlaceDTO) throws URISyntaxException {
        log.debug("REST request to save DistributionPlace : {}", distributionPlaceDTO);
        if (distributionPlaceDTO.getId() != null) {
            throw new BadRequestAlertException("A new distributionPlace cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DistributionPlaceDTO result = distributionPlaceService.save(distributionPlaceDTO);
        return ResponseEntity.created(new URI("/api/distribution-places/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /distribution-places : Updates an existing distributionPlace.
     *
     * @param distributionPlaceDTO the distributionPlaceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated distributionPlaceDTO,
     * or with status 400 (Bad Request) if the distributionPlaceDTO is not valid,
     * or with status 500 (Internal Server Error) if the distributionPlaceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/distribution-places")
    public ResponseEntity<DistributionPlaceDTO> updateDistributionPlace(@RequestBody DistributionPlaceDTO distributionPlaceDTO) throws URISyntaxException {
        log.debug("REST request to update DistributionPlace : {}", distributionPlaceDTO);
        if (distributionPlaceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DistributionPlaceDTO result = distributionPlaceService.save(distributionPlaceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, distributionPlaceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /distribution-places : get all the distributionPlaces.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of distributionPlaces in body
     */
    @GetMapping("/distribution-places")
    public List<DistributionPlaceDTO> getAllDistributionPlaces() {
        log.debug("REST request to get all DistributionPlaces");
        return distributionPlaceService.findAll();
    }

    /**
     * GET  /distribution-places/:id : get the "id" distributionPlace.
     *
     * @param id the id of the distributionPlaceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the distributionPlaceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/distribution-places/{id}")
    public ResponseEntity<DistributionPlaceDTO> getDistributionPlace(@PathVariable Long id) {
        log.debug("REST request to get DistributionPlace : {}", id);
        Optional<DistributionPlaceDTO> distributionPlaceDTO = distributionPlaceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(distributionPlaceDTO);
    }

    /**
     * DELETE  /distribution-places/:id : delete the "id" distributionPlace.
     *
     * @param id the id of the distributionPlaceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/distribution-places/{id}")
    public ResponseEntity<Void> deleteDistributionPlace(@PathVariable Long id) {
        log.debug("REST request to delete DistributionPlace : {}", id);
        distributionPlaceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
