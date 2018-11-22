package com.robmelfi.rcraspi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.robmelfi.rcraspi.service.ControllerService;
import com.robmelfi.rcraspi.web.rest.errors.BadRequestAlertException;
import com.robmelfi.rcraspi.web.rest.util.HeaderUtil;
import com.robmelfi.rcraspi.web.rest.util.PaginationUtil;
import com.robmelfi.rcraspi.service.dto.ControllerDTO;
import io.github.jhipster.web.util.ResponseUtil;
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
 * REST controller for managing Controller.
 */
@RestController
@RequestMapping("/api")
public class ControllerResource {

    private final Logger log = LoggerFactory.getLogger(ControllerResource.class);

    private static final String ENTITY_NAME = "controller";

    private final ControllerService controllerService;

    public ControllerResource(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    /**
     * POST  /controllers : Create a new controller.
     *
     * @param controllerDTO the controllerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new controllerDTO, or with status 400 (Bad Request) if the controller has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/controllers")
    @Timed
    public ResponseEntity<ControllerDTO> createController(@RequestBody ControllerDTO controllerDTO) throws URISyntaxException {
        log.debug("REST request to save Controller : {}", controllerDTO);
        if (controllerDTO.getId() != null) {
            throw new BadRequestAlertException("A new controller cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ControllerDTO result = controllerService.save(controllerDTO);
        return ResponseEntity.created(new URI("/api/controllers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /controllers : Updates an existing controller.
     *
     * @param controllerDTO the controllerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated controllerDTO,
     * or with status 400 (Bad Request) if the controllerDTO is not valid,
     * or with status 500 (Internal Server Error) if the controllerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/controllers")
    @Timed
    public ResponseEntity<ControllerDTO> updateController(@RequestBody ControllerDTO controllerDTO) throws URISyntaxException {
        log.debug("REST request to update Controller : {}", controllerDTO);
        if (controllerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ControllerDTO result = controllerService.save(controllerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, controllerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /controllers : get all the controllers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of controllers in body
     */
    @GetMapping("/controllers")
    @Timed
    public ResponseEntity<List<ControllerDTO>> getAllControllers(Pageable pageable) {
        log.debug("REST request to get a page of Controllers");
        Page<ControllerDTO> page = controllerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/controllers");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /controllers/:id : get the "id" controller.
     *
     * @param id the id of the controllerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the controllerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/controllers/{id}")
    @Timed
    public ResponseEntity<ControllerDTO> getController(@PathVariable Long id) {
        log.debug("REST request to get Controller : {}", id);
        Optional<ControllerDTO> controllerDTO = controllerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(controllerDTO);
    }

    /**
     * DELETE  /controllers/:id : delete the "id" controller.
     *
     * @param id the id of the controllerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/controllers/{id}")
    @Timed
    public ResponseEntity<Void> deleteController(@PathVariable Long id) {
        log.debug("REST request to delete Controller : {}", id);
        controllerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
