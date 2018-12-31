package com.robmelfi.rcraspi.service.mapper;

import com.robmelfi.rcraspi.domain.*;
import com.robmelfi.rcraspi.service.dto.NetatmoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Netatmo and its DTO NetatmoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NetatmoMapper extends EntityMapper<NetatmoDTO, Netatmo> {



    default Netatmo fromId(Long id) {
        if (id == null) {
            return null;
        }
        Netatmo netatmo = new Netatmo();
        netatmo.setId(id);
        return netatmo;
    }
}
