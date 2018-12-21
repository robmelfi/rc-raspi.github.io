package com.robmelfi.rcraspi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.robmelfi.rcraspi.service.TimerService;
import com.robmelfi.rcraspi.web.rest.errors.BadRequestAlertException;
import com.robmelfi.rcraspi.web.rest.util.HeaderUtil;
import com.robmelfi.rcraspi.service.dto.TimerDTO;
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
 * REST controller for managing Timer.
 */
@RestController
@RequestMapping("/api")
public class TimerResource {

    private final Logger log = LoggerFactory.getLogger(TimerResource.class);

    private static final String ENTITY_NAME = "timer";

    private final TimerService timerService;

    public TimerResource(TimerService timerService) {
        this.timerService = timerService;
    }

    /**
     * POST  /timers : Create a new timer.
     *
     * @param timerDTO the timerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new timerDTO, or with status 400 (Bad Request) if the timer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/timers")
    @Timed
    public ResponseEntity<TimerDTO> createTimer(@RequestBody TimerDTO timerDTO) throws URISyntaxException {
        log.debug("REST request to save Timer : {}", timerDTO);
        if (timerDTO.getId() != null) {
            throw new BadRequestAlertException("A new timer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TimerDTO result = timerService.save(timerDTO);
        return ResponseEntity.created(new URI("/api/timers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /timers : Updates an existing timer.
     *
     * @param timerDTO the timerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated timerDTO,
     * or with status 400 (Bad Request) if the timerDTO is not valid,
     * or with status 500 (Internal Server Error) if the timerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/timers")
    @Timed
    public ResponseEntity<TimerDTO> updateTimer(@RequestBody TimerDTO timerDTO) throws URISyntaxException {
        log.debug("REST request to update Timer : {}", timerDTO);
        if (timerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TimerDTO result = timerService.save(timerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, timerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /timers : get all the timers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of timers in body
     */
    @GetMapping("/timers")
    @Timed
    public List<TimerDTO> getAllTimers() {
        log.debug("REST request to get all Timers");
        return timerService.findAll();
    }

    /**
     * GET  /timers/:id : get the "id" timer.
     *
     * @param id the id of the timerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the timerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/timers/{id}")
    @Timed
    public ResponseEntity<TimerDTO> getTimer(@PathVariable Long id) {
        log.debug("REST request to get Timer : {}", id);
        Optional<TimerDTO> timerDTO = timerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timerDTO);
    }

    /**
     * DELETE  /timers/:id : delete the "id" timer.
     *
     * @param id the id of the timerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/timers/{id}")
    @Timed
    public ResponseEntity<Void> deleteTimer(@PathVariable Long id) {
        log.debug("REST request to delete Timer : {}", id);
        timerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
