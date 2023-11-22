package com.iconkalbar.broadcast.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iconkalbar.broadcast.TestConstant;
import com.iconkalbar.broadcast.factory.ModelFactory;
import com.iconkalbar.broadcast.model.PmSchedule;
import com.iconkalbar.broadcast.model.RecipientNumber;
import com.iconkalbar.broadcast.model.SitePOP;
import com.iconkalbar.broadcast.model.request.PmScheduleRequestDTO;

@SpringBootTest
public class PmScheduleServiceTest {

    @Autowired
    private PmScheduleService pmScheduleService;

    @Autowired
    private ModelFactory modelFactory;

    @Autowired
    private ObjectMapper objectMapper;

    private SimpleDateFormat sdformat = new SimpleDateFormat(TestConstant.dateFormat);

    @AfterEach
    void cleanUp() {
        modelFactory.purgeAllData();
    }

    @Test
    void fetchAllScheduleThisWeek_shouldFetchCorrectDataAccordingToMaintenanceDateAndSchedule() throws ParseException {
        Date PoP1Date = sdformat.parse("06-11-2023");
        Date PoP2Date = sdformat.parse("08-11-2023");
        Date PoP3Date = sdformat.parse("10-11-2023");
        String broadcastDate = "06-11-2023";
        PmSchedule schedule1 = modelFactory.generatePmSchedule(true, PoP1Date, null);
        PmSchedule schedule2 = modelFactory.generatePmSchedule(false, PoP2Date, null);
        PmSchedule schedule3 = modelFactory.generatePmSchedule(false, PoP3Date, null);

        List<PmSchedule> pmList = pmScheduleService.fetchAllScheduleThisWeek(broadcastDate);

        assertEquals(2, pmList.size());
        assertFalse(pmList.contains(schedule1));
        assertTrue(pmList.contains(schedule2));
        assertTrue(pmList.contains(schedule3));
    }

    @Test
    void updateRealizationDate_shouldUpdatePmScheduleData() throws ParseException, JsonMappingException, JsonProcessingException {
        String PoP1DateString = "06-11-2023";
        String realizationDateString = "10-11-2023";
        Date PoP1Date = sdformat.parse(PoP1DateString);
        PmSchedule schedule1 = modelFactory.generatePmSchedule(true, PoP1Date, null);
        PmScheduleRequestDTO request = PmScheduleRequestDTO.builder()
                                    .popId(schedule1.getSitePop().getPopId())
                                    .scheduledDate(PoP1DateString)
                                    .realizationDate(realizationDateString)
                                    .build();

        ResponseEntity<String> updatedDataResponse = pmScheduleService.updateRealizationDate(request);
        PmSchedule updatedData = objectMapper.readValue(updatedDataResponse.getBody(), PmSchedule.class);

        Date updatedRealizationDate = updatedData.getRealizationDate();
        assertEquals("10-11-2023", sdformat.format(updatedRealizationDate));
        assertEquals(true, updatedData.isMaintenanceDone());
    }

    @Test
    void updateRealizationDate_shouldThrowResourceNotFoundException_whenPoPIdNotFound() throws ParseException, JsonProcessingException {
        String PoP1DateString = "06-11-2023";
        String realizationDateString = "10-11-2023";
        Date PoP1Date = sdformat.parse(PoP1DateString);
        modelFactory.generatePmSchedule(true, PoP1Date, null);
        PmScheduleRequestDTO request = PmScheduleRequestDTO.builder()
                                    .popId("Wrong POP Id")
                                    .scheduledDate(PoP1DateString)
                                    .realizationDate(realizationDateString)
                                    .build();

        ResponseEntity<String> response = pmScheduleService.updateRealizationDate(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateRealizationDate_shouldThrowResourceNotFoundException_whenOmScheduleNotFound() throws ParseException, JsonProcessingException {
        String PoP1DateString = "06-11-2023";
        String wrongRequestDate = "08-11-2023";
        String realizationDateString = "10-11-2023";
        Date PoP1Date = sdformat.parse(PoP1DateString);
        PmSchedule pmSchedule = modelFactory.generatePmSchedule(true, PoP1Date, null);
        PmScheduleRequestDTO request = PmScheduleRequestDTO.builder()
                                    .popId(pmSchedule.getSitePop().getPopId())
                                    .scheduledDate(wrongRequestDate)
                                    .realizationDate(realizationDateString)
                                    .build();

        ResponseEntity<String> response = pmScheduleService.updateRealizationDate(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void addNewSchedule_shouldAddNewEntryInDB() throws JsonProcessingException, ParseException {
        RecipientNumber recipientNumber = modelFactory.generateRecipientNumber();
        SitePOP sitePOP = modelFactory.generateSitePOP();
        String scheduledDate = "12-11-2023";
        PmScheduleRequestDTO newPmScheduleRequest = PmScheduleRequestDTO.builder()
                                                    .popId(sitePOP.getPopId())
                                                    .recipientName(recipientNumber.getUserName())
                                                    .scheduledDate(scheduledDate)
                                                    .build();

        ResponseEntity<String> responseEntity = pmScheduleService.addNewSchedule(newPmScheduleRequest);
        PmScheduleRequestDTO responseBody = objectMapper.readValue(responseEntity.getBody(), PmScheduleRequestDTO.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertTrue(responseBody.getId()!=0);
    }
}
