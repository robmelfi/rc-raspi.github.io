package com.robmelfi.rcraspi.service;

import com.robmelfi.rcraspi.domain.Timer;
import com.robmelfi.rcraspi.repository.TimerRepository;
import com.robmelfi.rcraspi.service.dto.TimerDTO;
import com.robmelfi.rcraspi.service.mapper.TimerMapper;
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
 * Service Implementation for managing Timer.
 */
@Service
@Transactional
public class TimerService {

    private final Logger log = LoggerFactory.getLogger(TimerService.class);

    private final TimerRepository timerRepository;

    private final TimerManager timerManager;

    private final TimerMapper timerMapper;

    public TimerService(TimerRepository timerRepository, TimerManager timerManager, TimerMapper timerMapper) {
        this.timerRepository = timerRepository;
        this.timerManager = timerManager;
        this.timerMapper = timerMapper;
    }

    /**
     * Save a timer.
     *
     * @param timerDTO the entity to save
     * @return the persisted entity
     */
    public TimerDTO save(TimerDTO timerDTO) {
        log.debug("Request to save Timer : {}", timerDTO);

        Timer timer = timerMapper.toEntity(timerDTO);
        if(timerDTO.getId() != null) {
            this.timerManager.updateTimer(timer);
        }
        timer = timerRepository.save(timer);
        return timerMapper.toDto(timer);
    }

    /**
     * Get all the timers.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TimerDTO> findAll() {
        log.debug("Request to get all Timers");
        return timerRepository.findAll().stream()
            .map(timerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one timer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<TimerDTO> findOne(Long id) {
        log.debug("Request to get Timer : {}", id);
        return timerRepository.findById(id)
            .map(timerMapper::toDto);
    }

    /**
     * Delete the timer by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Timer : {}", id);
        timerRepository.deleteById(id);
    }
}
