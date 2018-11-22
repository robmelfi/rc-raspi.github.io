package com.robmelfi.rcraspi.service.mapper;

import com.robmelfi.rcraspi.domain.*;
import com.robmelfi.rcraspi.service.dto.ControllerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Controller and its DTO ControllerDTO.
 */
@Mapper(componentModel = "spring", uses = {PinMapper.class})
public interface ControllerMapper extends EntityMapper<ControllerDTO, Controller> {

    @Mapping(source = "pin.id", target = "pinId")
    @Mapping(source = "pin.name", target = "pinName")
    ControllerDTO toDto(Controller controller);

    @Mapping(source = "pinId", target = "pin")
    Controller toEntity(ControllerDTO controllerDTO);

    default Controller fromId(Long id) {
        if (id == null) {
            return null;
        }
        Controller controller = new Controller();
        controller.setId(id);
        return controller;
    }
}
