package com.robmelfi.rcraspi.repository;

import com.robmelfi.rcraspi.domain.Timer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Timer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimerRepository extends JpaRepository<Timer, Long> {

}
