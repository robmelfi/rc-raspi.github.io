package com.robmelfi.rcraspi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.robmelfi.rcraspi.service.SensorService;
import com.robmelfi.rcraspi.web.rest.errors.BadRequestAlertException;
import com.robmelfi.rcraspi.web.rest.util.HeaderUtil;
import com.robmelfi.rcraspi.service.dto.SensorDTO;
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
 * REST controller for managing Sensor.
 */
@RestController
@RequestMapping("/api")
public class SensorResource {

    private final Logger log = LoggerFactory.getLogger(SensorResource.class);

    private static final String ENTITY_NAME = "sensor";

    private final SensorService sensorService;

    public SensorResource(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    /**
     * POST  /sensors : Create a new sensor.
     *
     * @param sensorDTO the sensorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sensorDTO, or with status 400 (Bad Request) if the sensor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sensors")
    @Timed
    public ResponseEntity<SensorDTO> createSensor(@RequestBody SensorDTO sensorDTO) throws URISyntaxException {
        log.debug("REST request to save Sensor : {}", sensorDTO);
        if (sensorDTO.getId() != null) {
            throw new BadRequestAlertException("A new sensor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SensorDTO result = sensorService.save(sensorDTO);
        return ResponseEntity.created(new URI("/api/sensors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sensors : Updates an existing sensor.
     *
     * @param sensorDTO the sensorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sensorDTO,
     * or with status 400 (Bad Request) if the sensorDTO is not valid,
     * or with status 500 (Internal Server Error) if the sensorDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sensors")
    @Timed
    public ResponseEntity<SensorDTO> updateSensor(@RequestBody SensorDTO sensorDTO) throws URISyntaxException {
        log.debug("REST request to update Sensor : {}", sensorDTO);
        if (sensorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SensorDTO result = sensorService.save(sensorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sensorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sensors : get all the sensors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sensors in body
     */
    @GetMapping("/sensors")
    @Timed
    public List<SensorDTO> getAllSensors() {
        log.debug("REST request to get all Sensors");
        return sensorService.findAll();
    }

    /**
     * GET  /sensors/:id : get the "id" sensor.
     *
     * @param id the id of the sensorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sensorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sensors/{id}")
    @Timed
    public ResponseEntity<SensorDTO> getSensor(@PathVariable Long id) {
        log.debug("REST request to get Sensor : {}", id);
        Optional<SensorDTO> sensorDTO = sensorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sensorDTO);
    }

    /**
     * DELETE  /sensors/:id : delete the "id" sensor.
     *
     * @param id the id of the sensorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sensors/{id}")
    @Timed
    public ResponseEntity<Void> deleteSensor(@PathVariable Long id) {
        log.debug("REST request to delete Sensor : {}", id);
        sensorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
