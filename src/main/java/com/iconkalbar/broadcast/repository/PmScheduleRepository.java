package com.iconkalbar.broadcast.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.iconkalbar.broadcast.model.PmSchedule;

public interface PmScheduleRepository extends JpaRepository<PmSchedule, Integer>, JpaSpecificationExecutor<PmSchedule> {

    
}
