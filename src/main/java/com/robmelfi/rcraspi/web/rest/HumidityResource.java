package com.robmelfi.rcraspi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.robmelfi.rcraspi.service.HumidityService;
import com.robmelfi.rcraspi.web.rest.errors.BadRequestAlertException;
import com.robmelfi.rcraspi.web.rest.util.HeaderUtil;
import com.robmelfi.rcraspi.web.rest.util.PaginationUtil;
import com.robmelfi.rcraspi.service.dto.HumidityDTO;
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
 * REST controller for managing Humidity.
 */
@RestController
@RequestMapping("/api")
public class HumidityResource {

    private final Logger log = LoggerFactory.getLogger(HumidityResource.class);

    private static final String ENTITY_NAME = "humidity";

    private final HumidityService humidityService;

    public HumidityResource(HumidityService humidityService) {
        this.humidityService = humidityService;
    }

    /**
     * POST  /humidities : Create a new humidity.
     *
     * @param humidityDTO the humidityDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new humidityDTO, or with status 400 (Bad Request) if the humidity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/humidities")
    @Timed
    public ResponseEntity<HumidityDTO> createHumidity(@Valid @RequestBody HumidityDTO humidityDTO) throws URISyntaxException {
        log.debug("REST request to save Humidity : {}", humidityDTO);
        if (humidityDTO.getId() != null) {
            throw new BadRequestAlertException("A new humidity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HumidityDTO result = humidityService.save(humidityDTO);
        return ResponseEntity.created(new URI("/api/humidities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /humidities : Updates an existing humidity.
     *
     * @param humidityDTO the humidityDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated humidityDTO,
     * or with status 400 (Bad Request) if the humidityDTO is not valid,
     * or with status 500 (Internal Server Error) if the humidityDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect

    @PutMapping("/humidities")
    @Timed
    public ResponseEntity<HumidityDTO> updateHumidity(@Valid @RequestBody HumidityDTO humidityDTO) throws URISyntaxException {
        log.debug("REST request to update Humidity : {}", humidityDTO);
        if (humidityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HumidityDTO result = humidityService.save(humidityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, humidityDTO.getId().toString()))
            .body(result);
    }
    */
    /**
     * GET  /humidities : get all the humidities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of humidities in body
     */
    @GetMapping("/humidities")
    @Timed
    public ResponseEntity<List<HumidityDTO>> getAllHumidities(Pageable pageable) {
        log.debug("REST request to get a page of Humidities");
        Page<HumidityDTO> page = humidityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/humidities");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /humidities/last : get the last humidity.
     *
     * @return the entity
     */
    @GetMapping("/humidities/last")
    @Timed
    public HumidityDTO getLastHumidity() {
        log.debug("REST request to get last Humidity ");
        return humidityService.getLast();
    }

    /**
     * GET  /humidities/:id : get the "id" humidity.
     *
     * @param id the id of the humidityDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the humidityDTO, or with status 404 (Not Found)
     */
    @GetMapping("/humidities/{id}")
    @Timed
    public ResponseEntity<HumidityDTO> getHumidity(@PathVariable Long id) {
        log.debug("REST request to get Humidity : {}", id);
        Optional<HumidityDTO> humidityDTO = humidityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(humidityDTO);
    }

    /**
     * DELETE  /humidities/:id : delete the "id" humidity.
     *
     * @param id the id of the humidityDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/humidities/{id}")
    @Timed
    public ResponseEntity<Void> deleteHumidity(@PathVariable Long id) {
        log.debug("REST request to delete Humidity : {}", id);
        humidityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
