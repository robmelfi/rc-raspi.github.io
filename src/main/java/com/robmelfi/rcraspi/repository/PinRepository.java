package com.robmelfi.rcraspi.repository;

import com.robmelfi.rcraspi.domain.Pin;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Pin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PinRepository extends JpaRepository<Pin, Long> {

    @Query("SELECT p FROM Pin p LEFT JOIN Controller c ON p.id = c.pin where c is null")
    List<Pin> findAll();
}
