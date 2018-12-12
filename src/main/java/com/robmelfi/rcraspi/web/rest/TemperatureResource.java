package com.robmelfi.rcraspi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.robmelfi.rcraspi.service.TemperatureService;
import com.robmelfi.rcraspi.web.rest.errors.BadRequestAlertException;
import com.robmelfi.rcraspi.web.rest.util.HeaderUtil;
import com.robmelfi.rcraspi.web.rest.util.PaginationUtil;
import com.robmelfi.rcraspi.service.dto.TemperatureDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Temperature.
 */
@RestController
@RequestMapping("/api")
public class TemperatureResource {

    private final Logger log = LoggerFactory.getLogger(TemperatureResource.class);

    private static final String ENTITY_NAME = "temperature";

    private final TemperatureService temperatureService;

    public TemperatureResource(TemperatureService temperatureService) {
        this.temperatureService = temperatureService;
    }

    /**
     * POST  /temperatures : Create a new temperature.
     *
     * @param temperatureDTO the temperatureDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new temperatureDTO, or with status 400 (Bad Request) if the temperature has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/temperatures")
    @Timed
    public ResponseEntity<TemperatureDTO> createTemperature(@Valid @RequestBody TemperatureDTO temperatureDTO) throws URISyntaxException {
        log.debug("REST request to save Temperature : {}", temperatureDTO);
        if (temperatureDTO.getId() != null) {
            throw new BadRequestAlertException("A new temperature cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TemperatureDTO result = temperatureService.save(temperatureDTO);
        return ResponseEntity.created(new URI("/api/temperatures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /temperatures : Updates an existing temperature.
     *
     * @param temperatureDTO the temperatureDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated temperatureDTO,
     * or with status 400 (Bad Request) if the temperatureDTO is not valid,
     * or with status 500 (Internal Server Error) if the temperatureDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/temperatures")
    @Timed
    public ResponseEntity<TemperatureDTO> updateTemperature(@Valid @RequestBody TemperatureDTO temperatureDTO) throws URISyntaxException {
        log.debug("REST request to update Temperature : {}", temperatureDTO);
        if (temperatureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TemperatureDTO result = temperatureService.save(temperatureDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, temperatureDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /temperatures : get all the temperatures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of temperatures in body
     */
    @GetMapping("/temperatures")
    @Timed
    public ResponseEntity<List<TemperatureDTO>> getAllTemperatures(Pageable pageable) {
        log.debug("REST request to get a page of Temperatures");
        Page<TemperatureDTO> page = temperatureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/temperatures");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /temperatures/last : get the last temperature.
     *
     * @return the entity
     */
    @GetMapping("/temperatures/last")
    @Timed
    public TemperatureDTO getLastTemperature() {
        log.debug("REST request to get Last Temperature : {}");
        return temperatureService.getLast();
    }

    /**
     * GET  /temperatures/:id : get the "id" temperature.
     *
     * @param id the id of the temperatureDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the temperatureDTO, or with status 404 (Not Found)
     */
    @GetMapping("/temperatures/{id}")
    @Timed
    public ResponseEntity<TemperatureDTO> getTemperature(@PathVariable Long id) {
        log.debug("REST request to get Temperature : {}", id);
        Optional<TemperatureDTO> temperatureDTO = temperatureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(temperatureDTO);
    }

    /**
     * DELETE  /temperatures/:id : delete the "id" temperature.
     *
     * @param id the id of the temperatureDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/temperatures/{id}")
    @Timed
    public ResponseEntity<Void> deleteTemperature(@PathVariable Long id) {
        log.debug("REST request to delete Temperature : {}", id);
        temperatureService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
