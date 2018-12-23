package com.robmelfi.rcraspi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.robmelfi.rcraspi.domain.enumeration.IO;

/**
 * A Controller.
 */
@Entity
@Table(name = "controller")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Controller implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_mode")
    private IO mode;

    @Column(name = "state")
    private Boolean state;

    @OneToOne(optional = false)    @NotNull
    @JoinColumn(unique = true)
    private Pin pin;

    @OneToOne    @JoinColumn(unique = true)
    private Sensor sensor;

    @ManyToOne
    @JsonIgnoreProperties("controllers")
    private Timer timer;

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

    public Controller name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IO getMode() {
        return mode;
    }

    public Controller mode(IO mode) {
        this.mode = mode;
        return this;
    }

    public void setMode(IO mode) {
        this.mode = mode;
    }

    public Boolean getState() {
        return state;
    }

    public Controller state(Boolean state) {
        this.state = state;
        return this;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Pin getPin() {
        return pin;
    }

    public Controller pin(Pin pin) {
        this.pin = pin;
        return this;
    }

    public void setPin(Pin pin) {
        this.pin = pin;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public Controller sensor(Sensor sensor) {
        this.sensor = sensor;
        return this;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Timer getTimer() {
        return timer;
    }

    public Controller timer(Timer timer) {
        this.timer = timer;
        return this;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
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
        Controller controller = (Controller) o;
        if (controller.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), controller.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Controller{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", mode='" + getMode() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
