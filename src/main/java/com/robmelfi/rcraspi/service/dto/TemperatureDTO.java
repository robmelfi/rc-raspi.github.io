package com.robmelfi.rcraspi.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Temperature entity.
 */
public class TemperatureDTO implements Serializable {

    private Long id;

    private Float value;

    @NotNull
    private ZonedDateTime timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TemperatureDTO temperatureDTO = (TemperatureDTO) o;
        if (temperatureDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), temperatureDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TemperatureDTO{" +
            "id=" + getId() +
            ", value=" + getValue() +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
}
