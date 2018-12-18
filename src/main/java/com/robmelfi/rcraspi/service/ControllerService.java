package com.robmelfi.rcraspi.service;

import com.robmelfi.rcraspi.domain.Controller;
import com.robmelfi.rcraspi.repository.ControllerRepository;
import com.robmelfi.rcraspi.service.dto.ControllerDTO;
import com.robmelfi.rcraspi.service.mapper.ControllerMapper;
import com.robmelfi.rcraspi.timer.TimerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Controller.
 */
@Service
@Transactional
public class ControllerService {

    private final Logger log = LoggerFactory.getLogger(ControllerService.class);

    private final ControllerRepository controllerRepository;

    private final GpioService gpioService;

    private final TimerManager timerManager;

    private final ControllerMapper controllerMapper;

    public ControllerService(ControllerRepository controllerRepository, ControllerMapper controllerMapper, GpioService gpioService, TimerManager timerManager) {
        this.controllerRepository = controllerRepository;
        this.controllerMapper = controllerMapper;
        this.gpioService = gpioService;
        this.timerManager = timerManager;
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
        gpioService.addController(controller);
        timerManager.addTimer(controller);
        return controllerMapper.toDto(controller);
    }

    /**
     * Get all the controllers.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ControllerDTO> findAll() {
        log.debug("Request to get all Controllers");
        return controllerRepository.findAll().stream()
            .map(controllerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
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
        gpioService.removeController(id);
        timerManager.removeTimer(id);
        controllerRepository.deleteById(id);
    }
}
