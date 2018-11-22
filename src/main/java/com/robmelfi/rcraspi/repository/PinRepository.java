package com.robmelfi.rcraspi.repository;

import com.robmelfi.rcraspi.domain.Pin;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Pin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PinRepository extends JpaRepository<Pin, Long> {

}
