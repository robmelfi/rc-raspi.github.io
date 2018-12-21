package com.robmelfi.rcraspi.service.mapper;

import com.robmelfi.rcraspi.domain.*;
import com.robmelfi.rcraspi.service.dto.HumidityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Humidity and its DTO HumidityDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HumidityMapper extends EntityMapper<HumidityDTO, Humidity> {



    default Humidity fromId(Long id) {
        if (id == null) {
            return null;
        }
        Humidity humidity = new Humidity();
        humidity.setId(id);
        return humidity;
    }
}
