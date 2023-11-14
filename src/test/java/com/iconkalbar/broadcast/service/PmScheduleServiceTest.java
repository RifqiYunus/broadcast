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
        Date broadcastDate = sdformat.parse("06-11-2023");
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

        ResponseEntity<String> updatedDataResponse = pmScheduleService.updateRealizationDate(schedule1.getSitePop().getPopId(), PoP1DateString, realizationDateString);
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

        ResponseEntity<String> response = pmScheduleService.updateRealizationDate("Wrong POP Id", PoP1DateString, realizationDateString);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
