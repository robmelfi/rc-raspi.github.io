package com.robmelfi.rcraspi.service;

import com.robmelfi.rcraspi.domain.Controller;
import com.robmelfi.rcraspi.repository.ControllerRepository;
import com.robmelfi.rcraspi.service.dto.ControllerDTO;
import com.robmelfi.rcraspi.service.mapper.ControllerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Controller.
 */
@Service
@Transactional
public class ControllerService {

    private final Logger log = LoggerFactory.getLogger(ControllerService.class);

    private final ControllerRepository controllerRepository;

    private final ControllerMapper controllerMapper;

    public ControllerService(ControllerRepository controllerRepository, ControllerMapper controllerMapper) {
        this.controllerRepository = controllerRepository;
        this.controllerMapper = controllerMapper;
    }

    /**
     * Save a controller.
     *
     * @param controllerDTO the entity to save
     * @return the persisted entity
     */
    public ControllerDTO save(ControllerDTO controllerDTO) {
        log.debug("Request to save Controller : {}", controllerDTO);

        Controller controller = controllerMapper.toEntity(controllerDTO);
        controller = controllerRepository.save(controller);
        return controllerMapper.toDto(controller);
    }

    /**
     * Get all the controllers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ControllerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Controllers");
        return controllerRepository.findAll(pageable)
            .map(controllerMapper::toDto);
    }


    /**
     * Get one controller by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ControllerDTO> findOne(Long id) {
        log.debug("Request to get Controller : {}", id);
        return controllerRepository.findById(id)
            .map(controllerMapper::toDto);
    }

    /**
     * Delete the controller by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Controller : {}", id);
        controllerRepository.deleteById(id);
    }
}
