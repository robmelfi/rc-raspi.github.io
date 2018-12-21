package com.robmelfi.rcraspi.service;

import com.robmelfi.rcraspi.domain.Sensor;
import com.robmelfi.rcraspi.repository.SensorRepository;
import com.robmelfi.rcraspi.service.dto.SensorDTO;
import com.robmelfi.rcraspi.service.mapper.SensorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Sensor.
 */
@Service
@Transactional
public class SensorService {

    private final Logger log = LoggerFactory.getLogger(SensorService.class);

    private final SensorRepository sensorRepository;

    private final SensorMapper sensorMapper;

    public SensorService(SensorRepository sensorRepository, SensorMapper sensorMapper) {
        this.sensorRepository = sensorRepository;
        this.sensorMapper = sensorMapper;
    }

    /**
     * Save a sensor.
     *
     * @param sensorDTO the entity to save
     * @return the persisted entity
     */
    public SensorDTO save(SensorDTO sensorDTO) {
        log.debug("Request to save Sensor : {}", sensorDTO);

        Sensor sensor = sensorMapper.toEntity(sensorDTO);
        sensor = sensorRepository.save(sensor);
        return sensorMapper.toDto(sensor);
    }

    /**
     * Get all the sensors.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SensorDTO> findAll() {
        log.debug("Request to get all Sensors");
        return sensorRepository.findAll().stream()
            .map(sensorMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one sensor by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SensorDTO> findOne(Long id) {
        log.debug("Request to get Sensor : {}", id);
        return sensorRepository.findById(id)
            .map(sensorMapper::toDto);
    }

    /**
     * Delete the sensor by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Sensor : {}", id);
        sensorRepository.deleteById(id);
    }
}
