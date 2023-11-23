package com.iconkalbar.broadcast.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iconkalbar.broadcast.model.BroadcastNumber;
import com.iconkalbar.broadcast.model.PmSchedule;
import com.iconkalbar.broadcast.model.request.BroadcastRequestDTO;

@Service
public class BroadcastService {

    public BroadcastService(RestTemplate restTemplate, ObjectMapper objectMapper, PmScheduleService pmScheduleService, ContactService contactService) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.pmScheduleService = pmScheduleService;
        this.contactService = contactService;
    }

    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;

    private PmScheduleService pmScheduleService;

    private ContactService contactService;

    SimpleDateFormat sdFormat = new SimpleDateFormat(Constants.dateFormat);

    public ResponseEntity<String> blastMessage(PmSchedule pmSchedule, BroadcastNumber broadcastNumber) throws RestClientException, JsonProcessingException {
        BroadcastRequestDTO broadcastRequest = BroadcastRequestDTO.builder()
                                            .apiKey(Constants.API_KEY)
                                            .sender(broadcastNumber.getWaNumber())
                                            .number(pmSchedule.getRecipient().getWaNumber())
                                            .message("Hello World").build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<String>(objectMapper.writeValueAsString(broadcastRequest), headers);
        ResponseEntity<String> bResponseEntity = restTemplate.postForEntity(Constants.BLAST_URI, requestEntity, String.class);
        if(bResponseEntity.getStatusCode()==HttpStatus.OK){
            pmSchedule.setReminderSentTimes(pmSchedule.getReminderSentTimes() + 1);
            pmScheduleService.saveOrUpdatePmSchedule(pmSchedule);
        }
        return bResponseEntity;
    }

    @Scheduled(cron = "0 0 8 ? * MON *")
    public void scheduledBroadcast() throws ParseException, RestClientException, JsonProcessingException {
        String dateToday = sdFormat.format(new Date());
        List<PmSchedule> broadcastList = pmScheduleService.fetchAllScheduleThisWeek(dateToday);
        List<BroadcastNumber> broadcastNumbers = contactService.getBySenderName("Engineer KP");
        
        if(broadcastList.isEmpty() || broadcastNumbers.isEmpty()){
            return;
        }

        for (PmSchedule pmSchedule : broadcastList) {
            this.blastMessage(pmSchedule, broadcastNumbers.get(0));
        }

        return;
    }
    
}
