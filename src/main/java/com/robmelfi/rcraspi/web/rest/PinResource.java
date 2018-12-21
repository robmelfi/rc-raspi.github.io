package com.robmelfi.rcraspi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.robmelfi.rcraspi.service.PinService;
import com.robmelfi.rcraspi.web.rest.errors.BadRequestAlertException;
import com.robmelfi.rcraspi.web.rest.util.HeaderUtil;
import com.robmelfi.rcraspi.service.dto.PinDTO;
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
 * REST controller for managing Pin.
 */
@RestController
@RequestMapping("/api")
public class PinResource {

    private final Logger log = LoggerFactory.getLogger(PinResource.class);

    private static final String ENTITY_NAME = "pin";

    private final PinService pinService;

    public PinResource(PinService pinService) {
        this.pinService = pinService;
    }

    /**
     * POST  /pins : Create a new pin.
     *
     * @param pinDTO the pinDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pinDTO, or with status 400 (Bad Request) if the pin has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pins")
    @Timed
    public ResponseEntity<PinDTO> createPin(@RequestBody PinDTO pinDTO) throws URISyntaxException {
        log.debug("REST request to save Pin : {}", pinDTO);
        if (pinDTO.getId() != null) {
            throw new BadRequestAlertException("A new pin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PinDTO result = pinService.save(pinDTO);
        return ResponseEntity.created(new URI("/api/pins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pins : Updates an existing pin.
     *
     * @param pinDTO the pinDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pinDTO,
     * or with status 400 (Bad Request) if the pinDTO is not valid,
     * or with status 500 (Internal Server Error) if the pinDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pins")
    @Timed
    public ResponseEntity<PinDTO> updatePin(@RequestBody PinDTO pinDTO) throws URISyntaxException {
        log.debug("REST request to update Pin : {}", pinDTO);
        if (pinDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PinDTO result = pinService.save(pinDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pinDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pins : get all the pins.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pins in body
     */
    @GetMapping("/pins")
    @Timed
    public List<PinDTO> getAllPins() {
        log.debug("REST request to get all Pins");
        return pinService.findAll();
    }

    /**
     * GET  /pins/:id : get the "id" pin.
     *
     * @param id the id of the pinDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pinDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pins/{id}")
    @Timed
    public ResponseEntity<PinDTO> getPin(@PathVariable Long id) {
        log.debug("REST request to get Pin : {}", id);
        Optional<PinDTO> pinDTO = pinService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pinDTO);
    }

    /**
     * DELETE  /pins/:id : delete the "id" pin.
     *
     * @param id the id of the pinDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pins/{id}")
    @Timed
    public ResponseEntity<Void> deletePin(@PathVariable Long id) {
        log.debug("REST request to delete Pin : {}", id);
        pinService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
