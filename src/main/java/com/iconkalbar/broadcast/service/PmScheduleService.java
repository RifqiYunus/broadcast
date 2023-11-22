package com.iconkalbar.broadcast.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iconkalbar.broadcast.model.PmSchedule;
import com.iconkalbar.broadcast.model.RecipientNumber;
import com.iconkalbar.broadcast.model.SitePOP;
import com.iconkalbar.broadcast.model.request.PmScheduleRequestDTO;
import com.iconkalbar.broadcast.repository.PmScheduleRepository;

@Service
public class PmScheduleService {
    
    @Autowired
    private PmScheduleRepository pmScheduleRepository;

    @Autowired
    private SitePoPService sitePoPService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ObjectMapper objectMapper;

    private SimpleDateFormat sdFormat = new SimpleDateFormat(Constants.dateFormat);

    public List<PmSchedule> fetchAllScheduleThisWeek(String dateString) throws ParseException {
        Date scheduleDate = sdFormat.parse(dateString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(scheduleDate);
        cal.add(Calendar.DAY_OF_MONTH,5);
        Date endOfWeek = cal.getTime();
        return pmScheduleRepository.findByScheduledDateBeforeAndIsMaintenanceDone(endOfWeek, false);
    }

    public PmSchedule saveOrUpdatePmSchedule(PmSchedule pmSchedule) {
        return pmScheduleRepository.save(pmSchedule);
    }

    public ResponseEntity<String> updateRealizationDate(PmScheduleRequestDTO pmScheduleRequest) throws ParseException, JsonProcessingException {
        SitePOP sitePOP;
        PmSchedule pmSchedule;
        
        try {
            sitePOP = sitePoPService.fetchByPopId(pmScheduleRequest.getPopId()).get(0);
        } catch (Exception e) {
            return new ResponseEntity<>("POP with ID " + pmScheduleRequest.getPopId() + " not found", HttpStatus.NOT_FOUND);
        }
        
        try {
            pmSchedule = pmScheduleRepository.findBySitePopAndScheduledDate(sitePOP, sdFormat.parse(pmScheduleRequest.getScheduledDate())).get(0);
        } catch (Exception e) {
            return new ResponseEntity<>("Schedule not found", HttpStatus.NOT_FOUND);
        }
        
        pmSchedule.setRealizationDate(sdFormat.parse(pmScheduleRequest.getRealizationDate()));
        pmSchedule.setMaintenanceDone(true);
        PmSchedule updatedSchedule = pmScheduleRepository.save(pmSchedule);
        pmScheduleRequest.setMaintenanceDone(pmSchedule.isMaintenanceDone());
        pmScheduleRequest.setId(pmSchedule.getId());
        
        return new ResponseEntity<>(objectMapper.writeValueAsString(updatedSchedule), HttpStatus.CREATED);
    }

    public ResponseEntity<String> addNewSchedule(PmScheduleRequestDTO newPmScheduleRequest) throws ParseException, JsonProcessingException {
        SitePOP sitePOP;
        RecipientNumber recipientNumber;
        
        try {
            sitePOP = sitePoPService.fetchByPopId(newPmScheduleRequest.getPopId()).get(0);
        } catch (Exception e) {
            return new ResponseEntity<>("POP with ID " + newPmScheduleRequest.getPopId() + " not found", HttpStatus.NOT_FOUND);
        }

        try {
            recipientNumber = contactService.getByRecipientName(newPmScheduleRequest.getRecipientName()).get(0);
        } catch (Exception e) {
            return new ResponseEntity<>("No recipient contact by name of " + newPmScheduleRequest.getRecipientName(), HttpStatus.NOT_FOUND);
        }

        PmSchedule pmSchedule = PmSchedule.builder()
                                .sitePop(sitePOP)
                                .recipient(recipientNumber)
                                .scheduledDate(sdFormat.parse(newPmScheduleRequest.getScheduledDate()))
                                .realizationDate(null)
                                .isMaintenanceDone(false)
                                .reminderSentTimes(0)
                                .build();

        pmSchedule = this.saveOrUpdatePmSchedule(pmSchedule);

        newPmScheduleRequest.setId(pmSchedule.getId());
        
        String responseBody = objectMapper.writeValueAsString(newPmScheduleRequest);

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }
}
