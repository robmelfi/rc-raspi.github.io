package com.robmelfi.rcraspi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.robmelfi.rcraspi.domain.enumeration.Repeat;

/**
 * A Timer.
 */
@Entity
@Table(name = "timer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Timer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "jhi_start", nullable = false)
    private ZonedDateTime start;

    @NotNull
    @Column(name = "jhi_stop", nullable = false)
    private ZonedDateTime stop;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_repeat")
    private Repeat repeat;

    @OneToMany(mappedBy = "timer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Controller> controllers = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Timer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public Timer start(ZonedDateTime start) {
        this.start = start;
        return this;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getStop() {
        return stop;
    }

    public Timer stop(ZonedDateTime stop) {
        this.stop = stop;
        return this;
    }

    public void setStop(ZonedDateTime stop) {
        this.stop = stop;
    }

    public Repeat getRepeat() {
        return repeat;
    }

    public Timer repeat(Repeat repeat) {
        this.repeat = repeat;
        return this;
    }

    public void setRepeat(Repeat repeat) {
        this.repeat = repeat;
    }

    public Set<Controller> getControllers() {
        return controllers;
    }

    public Timer controllers(Set<Controller> controllers) {
        this.controllers = controllers;
        return this;
    }

    public Timer addController(Controller controller) {
        this.controllers.add(controller);
        controller.setTimer(this);
        return this;
    }

    public Timer removeController(Controller controller) {
        this.controllers.remove(controller);
        controller.setTimer(null);
        return this;
    }

    public void setControllers(Set<Controller> controllers) {
        this.controllers = controllers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Timer timer = (Timer) o;
        if (timer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), timer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Timer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", start='" + getStart() + "'" +
            ", stop='" + getStop() + "'" +
            ", repeat='" + getRepeat() + "'" +
            "}";
    }
}
