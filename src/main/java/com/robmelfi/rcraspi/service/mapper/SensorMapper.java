package com.robmelfi.rcraspi.service.mapper;

import com.robmelfi.rcraspi.domain.*;
import com.robmelfi.rcraspi.service.dto.SensorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Sensor and its DTO SensorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SensorMapper extends EntityMapper<SensorDTO, Sensor> {



    default Sensor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Sensor sensor = new Sensor();
        sensor.setId(id);
        return sensor;
    }
}
