package com.robmelfi.rcraspi.service;

import com.robmelfi.rcraspi.domain.Humidity;
import com.robmelfi.rcraspi.repository.HumidityRepository;
import com.robmelfi.rcraspi.service.dto.HumidityDTO;
import com.robmelfi.rcraspi.service.mapper.HumidityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Humidity.
 */
@Service
@Transactional
public class HumidityService {

    private final Logger log = LoggerFactory.getLogger(HumidityService.class);

    private final HumidityRepository humidityRepository;

    private final HumidityMapper humidityMapper;

    public HumidityService(HumidityRepository humidityRepository, HumidityMapper humidityMapper) {
        this.humidityRepository = humidityRepository;
        this.humidityMapper = humidityMapper;
    }

    /**
     * Save a humidity.
     *
     * @param humidityDTO the entity to save
     * @return the persisted entity
     */
    public HumidityDTO save(HumidityDTO humidityDTO) {
        log.debug("Request to save Humidity : {}", humidityDTO);

        Humidity humidity = humidityMapper.toEntity(humidityDTO);
        humidity = humidityRepository.save(humidity);
        return humidityMapper.toDto(humidity);
    }

    /**
     * Get all the humidities.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HumidityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Humidities");
        return humidityRepository.findAll(pageable)
            .map(humidityMapper::toDto);
    }


    /**
     * Get one humidity by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<HumidityDTO> findOne(Long id) {
        log.debug("Request to get Humidity : {}", id);
        return humidityRepository.findById(id)
            .map(humidityMapper::toDto);
    }

    /**
     * Delete the humidity by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Humidity : {}", id);
        humidityRepository.deleteById(id);
    }
}
