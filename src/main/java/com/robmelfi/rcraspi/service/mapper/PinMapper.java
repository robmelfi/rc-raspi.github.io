package com.robmelfi.rcraspi.service.mapper;

import com.robmelfi.rcraspi.domain.*;
import com.robmelfi.rcraspi.service.dto.PinDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Pin and its DTO PinDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PinMapper extends EntityMapper<PinDTO, Pin> {



    default Pin fromId(Long id) {
        if (id == null) {
            return null;
        }
        Pin pin = new Pin();
        pin.setId(id);
        return pin;
    }
}
