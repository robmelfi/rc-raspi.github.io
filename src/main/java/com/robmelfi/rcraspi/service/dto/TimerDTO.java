package com.robmelfi.rcraspi.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;
import com.robmelfi.rcraspi.domain.enumeration.Repeat;

/**
 * A DTO for the Timer entity.
 */
public class TimerDTO implements Serializable {

    private Long id;

    private String name;

    private ZonedDateTime start;

    private ZonedDateTime stop;

    private Repeat repeat;

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

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getStop() {
        return stop;
    }

    public void setStop(ZonedDateTime stop) {
        this.stop = stop;
    }

    public Repeat getRepeat() {
        return repeat;
    }

    public void setRepeat(Repeat repeat) {
        this.repeat = repeat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TimerDTO timerDTO = (TimerDTO) o;
        if (timerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), timerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TimerDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", start='" + getStart() + "'" +
            ", stop='" + getStop() + "'" +
            ", repeat='" + getRepeat() + "'" +
            "}";
    }
}
