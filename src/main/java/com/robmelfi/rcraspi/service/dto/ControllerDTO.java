package com.robmelfi.rcraspi.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.robmelfi.rcraspi.domain.enumeration.IO;

/**
 * A DTO for the Controller entity.
 */
public class ControllerDTO implements Serializable {

    private Long id;

    private String name;

    private IO mode;

    private Long pinId;

    private String pinName;

    private boolean status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IO getMode() {
        return mode;
    }

    public void setMode(IO mode) {
        this.mode = mode;
    }

    public Long getPinId() {
        return pinId;
    }

    public void setPinId(Long pinId) {
        this.pinId = pinId;
    }

    public String getPinName() {
        return pinName;
    }

    public void setPinName(String pinName) {
        this.pinName = pinName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ControllerDTO controllerDTO = (ControllerDTO) o;
        if (controllerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), controllerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ControllerDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", mode='" + getMode() + "'" +
            ", pin=" + getPinId() +
            ", pin='" + getPinName() + "'" +
            "}";
    }
}
