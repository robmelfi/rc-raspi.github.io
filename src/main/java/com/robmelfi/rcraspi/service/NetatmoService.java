package com.robmelfi.rcraspi.service;

import com.robmelfi.rcraspi.domain.Netatmo;
import com.robmelfi.rcraspi.netatmo.NetatmoScheduleService;
import com.robmelfi.rcraspi.repository.NetatmoRepository;
import com.robmelfi.rcraspi.service.dto.NetatmoDTO;
import com.robmelfi.rcraspi.service.mapper.NetatmoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Netatmo.
 */
@Service
@Transactional
public class NetatmoService {

    private final Logger log = LoggerFactory.getLogger(NetatmoService.class);

    private final NetatmoRepository netatmoRepository;
    private final NetatmoScheduleService netatmoScheduleService;

    private final NetatmoMapper netatmoMapper;

    public NetatmoService(NetatmoRepository netatmoRepository, NetatmoScheduleService netatmoScheduleService, NetatmoMapper netatmoMapper) {
        this.netatmoRepository = netatmoRepository;
        this.netatmoScheduleService = netatmoScheduleService;
        this.netatmoMapper = netatmoMapper;
        this.loadNetatmoCredentials();
    }

    private void loadNetatmoCredentials() {
        NetatmoDTO result = null;
        List<NetatmoDTO> netatmoDTOList = this.findAll();
        if (netatmoDTOList.size() == 1) {
            result = netatmoDTOList.get(0);
        }
        if (result != null) {
            this.netatmoScheduleService.initNetatmoHttpClient(result);
            this.netatmoScheduleService.startReadNetatmoStatus();
        }
    }

    /**
     * Save a netatmo.
     *
     * @param netatmoDTO the entity to save
     * @return the persisted entity
     */
    public NetatmoDTO save(NetatmoDTO netatmoDTO) {
        log.debug("Request to save Netatmo : {}", netatmoDTO);

        Netatmo netatmo = netatmoMapper.toEntity(netatmoDTO);
        netatmo = netatmoRepository.save(netatmo);
        this.loadNetatmoCredentials();
        return netatmoMapper.toDto(netatmo);
    }

    /**
     * Get all the netatmos.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<NetatmoDTO> findAll() {
        log.debug("Request to get all Netatmos");
        return netatmoRepository.findAll().stream()
            .map(netatmoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one netatmo by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<NetatmoDTO> findOne(Long id) {
        log.debug("Request to get Netatmo : {}", id);
        return netatmoRepository.findById(id)
            .map(netatmoMapper::toDto);
    }

    /**
     * Delete the netatmo by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Netatmo : {}", id);
        this.netatmoScheduleService.resetNetatmoHttpClient();
        netatmoRepository.deleteById(id);
    }
}
