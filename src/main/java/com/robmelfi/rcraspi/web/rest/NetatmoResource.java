package com.robmelfi.rcraspi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.robmelfi.rcraspi.service.NetatmoService;
import com.robmelfi.rcraspi.web.rest.errors.BadRequestAlertException;
import com.robmelfi.rcraspi.web.rest.util.HeaderUtil;
import com.robmelfi.rcraspi.service.dto.NetatmoDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Netatmo.
 */
@RestController
@RequestMapping("/api")
public class NetatmoResource {

    private final Logger log = LoggerFactory.getLogger(NetatmoResource.class);

    private static final String ENTITY_NAME = "netatmo";

    private final NetatmoService netatmoService;

    public NetatmoResource(NetatmoService netatmoService) {
        this.netatmoService = netatmoService;
    }

    /**
     * POST  /netatmos : Create a new netatmo.
     *
     * @param netatmoDTO the netatmoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new netatmoDTO, or with status 400 (Bad Request) if the netatmo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/netatmos")
    @Timed
    public ResponseEntity<NetatmoDTO> createNetatmo(@Valid @RequestBody NetatmoDTO netatmoDTO) throws URISyntaxException {
        log.debug("REST request to save Netatmo : {}", netatmoDTO);
        if (netatmoDTO.getId() != null) {
            throw new BadRequestAlertException("A new netatmo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NetatmoDTO result = netatmoService.save(netatmoDTO);
        return ResponseEntity.created(new URI("/api/netatmos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /netatmos : Updates an existing netatmo.
     *
     * @param netatmoDTO the netatmoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated netatmoDTO,
     * or with status 400 (Bad Request) if the netatmoDTO is not valid,
     * or with status 500 (Internal Server Error) if the netatmoDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/netatmos")
    @Timed
    public ResponseEntity<NetatmoDTO> updateNetatmo(@Valid @RequestBody NetatmoDTO netatmoDTO) throws URISyntaxException {
        log.debug("REST request to update Netatmo : {}", netatmoDTO);
        if (netatmoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NetatmoDTO result = netatmoService.save(netatmoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, netatmoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /netatmos : get all the netatmos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of netatmos in body
     */
    @GetMapping("/netatmos")
    @Timed
    public List<NetatmoDTO> getAllNetatmos() {
        log.debug("REST request to get all Netatmos");
        return netatmoService.findAll();
    }

    /**
     * GET  /netatmos/:id : get the "id" netatmo.
     *
     * @param id the id of the netatmoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the netatmoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/netatmos/{id}")
    @Timed
    public ResponseEntity<NetatmoDTO> getNetatmo(@PathVariable Long id) {
        log.debug("REST request to get Netatmo : {}", id);
        Optional<NetatmoDTO> netatmoDTO = netatmoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(netatmoDTO);
    }

    /**
     * DELETE  /netatmos/:id : delete the "id" netatmo.
     *
     * @param id the id of the netatmoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/netatmos/{id}")
    @Timed
    public ResponseEntity<Void> deleteNetatmo(@PathVariable Long id) {
        log.debug("REST request to delete Netatmo : {}", id);
        netatmoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
