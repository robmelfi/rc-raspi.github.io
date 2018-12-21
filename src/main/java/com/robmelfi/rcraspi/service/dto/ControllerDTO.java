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

    private Boolean state;

    private Long pinId;

    private String pinName;

    private Long sensorId;

    private String sensorName;

    private Long timerId;

    private String timerName;

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

    public Boolean isState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
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

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public Long getTimerId() {
        return timerId;
    }

    public void setTimerId(Long timerId) {
        this.timerId = timerId;
    }

    public String getTimerName() {
        return timerName;
    }

    public void setTimerName(String timerName) {
        this.timerName = timerName;
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
            ", state='" + isState() + "'" +
            ", pin=" + getPinId() +
            ", pin='" + getPinName() + "'" +
            ", sensor=" + getSensorId() +
            ", sensor='" + getSensorName() + "'" +
            ", timer=" + getTimerId() +
            ", timer='" + getTimerName() + "'" +
            "}";
    }
}
