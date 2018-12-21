package com.robmelfi.rcraspi.repository;

import com.robmelfi.rcraspi.domain.Humidity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Humidity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HumidityRepository extends JpaRepository<Humidity, Long> {

}
