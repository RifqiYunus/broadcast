package com.iconkalbar.broadcast.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.iconkalbar.broadcast.model.PmSchedule;
import java.util.List;
import java.util.Date;


public interface PmScheduleRepository extends JpaRepository<PmSchedule, Integer>, JpaSpecificationExecutor<PmSchedule> {

    List<PmSchedule> findByScheduledDateBeforeAndIsMaintenanceDone(Date scheduledDate, boolean isMaintenanceDone);
    
}
