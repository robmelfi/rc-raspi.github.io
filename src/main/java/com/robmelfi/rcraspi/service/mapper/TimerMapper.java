package com.robmelfi.rcraspi.service.mapper;

import com.robmelfi.rcraspi.domain.*;
import com.robmelfi.rcraspi.service.dto.TimerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Timer and its DTO TimerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TimerMapper extends EntityMapper<TimerDTO, Timer> {


    @Mapping(target = "controllers", ignore = true)
    Timer toEntity(TimerDTO timerDTO);

    default Timer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Timer timer = new Timer();
        timer.setId(id);
        return timer;
    }
}
