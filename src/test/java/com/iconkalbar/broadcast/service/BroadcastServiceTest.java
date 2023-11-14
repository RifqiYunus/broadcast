package com.iconkalbar.broadcast.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.iconkalbar.broadcast.TestConstant;
import com.iconkalbar.broadcast.factory.ModelFactory;
import com.iconkalbar.broadcast.model.BroadcastNumber;
import com.iconkalbar.broadcast.model.PmSchedule;
import com.iconkalbar.broadcast.repository.PmScheduleRepository;

@SpringBootTest
public class BroadcastServiceTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private PmScheduleRepository pmScheduleRepository;

    @InjectMocks
    @Autowired
    private BroadcastService broadcastService;

    @Autowired
    private ModelFactory modelFactory;

    SimpleDateFormat formatter = new SimpleDateFormat(TestConstant.dateFormat);

    @Test
    void blastMessage_shouldHitRestApiWithCorrectPayload() throws ParseException, RestClientException, JsonProcessingException {
        String responseBody = "result";
        PmSchedule pmSchedule = modelFactory.generatePmSchedule(false, formatter.parse("12-11-2023"), null);
        BroadcastNumber broadcastNumber = modelFactory.generateBroadcastNumber();
        Mockito.when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(generateResponseEntityObject(responseBody, HttpStatus.OK));

        ResponseEntity<String> response = broadcastService.blastMessage(pmSchedule, broadcastNumber);

        List<PmSchedule> updatedSchedule = pmScheduleRepository.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.verify(restTemplate).postForEntity(eq(Constants.BLAST_URI), any(), eq(String.class));
        assertEquals(1, updatedSchedule.get(0).getReminderSentTimes());
    }

    private ResponseEntity<String> generateResponseEntityObject(String response, HttpStatus httpStatus){
        return new ResponseEntity<>(response, httpStatus);
    }
}
