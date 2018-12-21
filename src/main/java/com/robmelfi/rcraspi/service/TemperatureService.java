package com.robmelfi.rcraspi.service;

import com.robmelfi.rcraspi.domain.Temperature;
import com.robmelfi.rcraspi.repository.TemperatureRepository;
import com.robmelfi.rcraspi.service.dto.TemperatureDTO;
import com.robmelfi.rcraspi.service.mapper.TemperatureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Temperature.
 */
@Service
@Transactional
public class TemperatureService {

    private final Logger log = LoggerFactory.getLogger(TemperatureService.class);

    private final TemperatureRepository temperatureRepository;

    private final TemperatureMapper temperatureMapper;

    public TemperatureService(TemperatureRepository temperatureRepository, TemperatureMapper temperatureMapper) {
        this.temperatureRepository = temperatureRepository;
        this.temperatureMapper = temperatureMapper;
    }

    /**
     * Save a temperature.
     *
     * @param temperatureDTO the entity to save
     * @return the persisted entity
     */
    public TemperatureDTO save(TemperatureDTO temperatureDTO) {
        log.debug("Request to save Temperature : {}", temperatureDTO);

        Temperature temperature = temperatureMapper.toEntity(temperatureDTO);
        temperature = temperatureRepository.save(temperature);
        return temperatureMapper.toDto(temperature);
    }

    /**
     * Get all the temperatures.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TemperatureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Temperatures");
        return temperatureRepository.findAll(pageable)
            .map(temperatureMapper::toDto);
    }


    /**
     * Get one temperature by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<TemperatureDTO> findOne(Long id) {
        log.debug("Request to get Temperature : {}", id);
        return temperatureRepository.findById(id)
            .map(temperatureMapper::toDto);
    }

    /**
     * Delete the temperature by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Temperature : {}", id);
        temperatureRepository.deleteById(id);
    }
}
