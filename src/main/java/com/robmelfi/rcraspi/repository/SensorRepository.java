package com.robmelfi.rcraspi.repository;

import com.robmelfi.rcraspi.domain.Sensor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Sensor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {

}
