package com.robmelfi.rcraspi.repository;

import com.robmelfi.rcraspi.domain.Sensor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Sensor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {

    @Query("SELECT s FROM Sensor s LEFT JOIN Controller c ON s.id = c.sensor where c is null")
    List<Sensor> findAll();
}
