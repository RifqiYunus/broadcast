package com.iconkalbar.broadcast.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iconkalbar.broadcast.model.PmSchedule;
import com.iconkalbar.broadcast.repository.PmScheduleRepository;

@Service
public class PmScheduleService {
    
    @Autowired
    private PmScheduleRepository pmScheduleRepository;

    List<PmSchedule> fetchAllScheduleThisWeek(Date scheduledDate) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH,5);
        Date endOfWeek = cal.getTime();
        return pmScheduleRepository.findByScheduledDateBeforeAndIsMaintenanceDone(endOfWeek, false);
    }

    PmSchedule saveOrUpdatePmSchedule(PmSchedule pmSchedule) {
        return pmScheduleRepository.save(pmSchedule);
    }
}
