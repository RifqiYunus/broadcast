package com.iconkalbar.broadcast.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.iconkalbar.broadcast.model.PmSchedule;
import com.iconkalbar.broadcast.model.SitePOP;
import com.iconkalbar.broadcast.model.request.PmScheduleRequestDTO;
import com.iconkalbar.broadcast.service.PmScheduleService;
import com.iconkalbar.broadcast.service.SitePoPService;

@RestController
@RequestMapping("pm-isp")
public class ISPScheduleController {
    
    @Autowired
    private PmScheduleService pmScheduleService;

    @Autowired
    private SitePoPService sitePoPService;

    @GetMapping("/schedules")
    List<PmSchedule> fetchUnfinishedSchedule(@RequestParam String date) throws ParseException {
        return pmScheduleService.fetchAllScheduleThisWeek(date);
    }

    @PostMapping("/schedules")
    ResponseEntity<String> postNewSchedule(@RequestBody PmScheduleRequestDTO request) throws JsonProcessingException, ParseException {
        return pmScheduleService.addNewSchedule(request);
    }

    @PutMapping("/schedules")
    ResponseEntity<String> updateRealizationDate(@RequestBody PmScheduleRequestDTO request) throws JsonProcessingException, ParseException {
        return pmScheduleService.updateRealizationDate(request);
    }

    @PostMapping("/sites")
    ResponseEntity<SitePOP> postNewSite(@RequestBody SitePOP sitePOP) {
        return new ResponseEntity<>(sitePoPService.saveOrUpdateSitePOP(sitePOP), HttpStatus.CREATED);
    }
}
