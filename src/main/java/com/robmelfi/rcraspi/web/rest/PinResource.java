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

}
