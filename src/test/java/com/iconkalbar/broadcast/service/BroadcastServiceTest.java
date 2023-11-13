package com.iconkalbar.broadcast.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
import com.iconkalbar.broadcast.model.BroadcastNumber;
import com.iconkalbar.broadcast.model.PmSchedule;
import com.iconkalbar.broadcast.model.RecipientNumber;
import com.iconkalbar.broadcast.model.SitePOP;

@SpringBootTest
public class BroadcastServiceTest {

    @MockBean
    private RestTemplate restTemplate;

    @InjectMocks
    @Autowired
    private BroadcastService broadcastService;

    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    @Test
    void blastMessage_shouldHitRestApiWithCorrectPayload() throws ParseException, RestClientException, JsonProcessingException {
        SitePOP sitePOP = SitePOP.builder()
                            .name("Pikitring")
                            .popId("PTK001")
                            .type("Shelter")
                            .build();
        RecipientNumber recipientNumber1 = RecipientNumber.builder()
                            .userName("Serpo Pontianak")
                            .waNumber("6283831390016")
                            .build();
        PmSchedule pmSchedule = PmSchedule.builder()
                                .recipient(recipientNumber1)
                                .sitePop(sitePOP)
                                .isReminderSent(false)
                                .scheduledDate(formatter.parse("12-11-2023"))
                                .build();
        BroadcastNumber broadcastNumber = BroadcastNumber.builder()
                            .userName("Har Internal")
                            .waNumber("6285707234237")
                            .build();
                            String responseBody = "result";
        Mockito.when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(generateResponseEntityObject(responseBody, HttpStatus.OK));

        ResponseEntity<String> response = broadcastService.blastMessage(pmSchedule, broadcastNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private ResponseEntity<String> generateResponseEntityObject(String response, HttpStatus httpStatus){
        return new ResponseEntity<>(response, httpStatus);
    }
}
