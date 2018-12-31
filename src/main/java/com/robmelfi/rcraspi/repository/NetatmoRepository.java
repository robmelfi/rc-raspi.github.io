package com.robmelfi.rcraspi.repository;

import com.robmelfi.rcraspi.domain.Netatmo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Netatmo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NetatmoRepository extends JpaRepository<Netatmo, Long> {

}
