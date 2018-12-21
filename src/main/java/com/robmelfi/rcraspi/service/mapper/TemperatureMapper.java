package com.robmelfi.rcraspi.service.mapper;

import com.robmelfi.rcraspi.domain.*;
import com.robmelfi.rcraspi.service.dto.TemperatureDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Temperature and its DTO TemperatureDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TemperatureMapper extends EntityMapper<TemperatureDTO, Temperature> {



    default Temperature fromId(Long id) {
        if (id == null) {
            return null;
        }
        Temperature temperature = new Temperature();
        temperature.setId(id);
        return temperature;
    }
}
