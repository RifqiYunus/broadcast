package com.iconkalbar.broadcast.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iconkalbar.broadcast.TestConstant;
import com.iconkalbar.broadcast.factory.ModelFactory;
import com.iconkalbar.broadcast.model.PmSchedule;
import com.iconkalbar.broadcast.model.RecipientNumber;
import com.iconkalbar.broadcast.model.SitePOP;
import com.iconkalbar.broadcast.model.request.PmScheduleRequestDTO;
import com.iconkalbar.broadcast.repository.PmScheduleRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ISPScheduleControllerTest {

    @Autowired
    private ModelFactory modelFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PmScheduleRepository pmScheduleRepository;

    private SimpleDateFormat sdformat = new SimpleDateFormat(TestConstant.dateFormat);

    @AfterEach
    void cleanUp() {
        modelFactory.purgeAllData();
    }

    @Test
    void fetchUnfinishedSchedule_shouldGetAllIncompleteSchedule() throws Exception {
        Date PoP1Date = sdformat.parse("06-11-2023");
        Date PoP2Date = sdformat.parse("08-11-2023");
        Date PoP3Date = sdformat.parse("10-11-2023");
        String broadcastDate = "06-11-2023";
        PmSchedule schedule1 = modelFactory.generatePmSchedule(true, PoP1Date, null);
        PmSchedule schedule2 = modelFactory.generatePmSchedule(false, PoP2Date, null);
        PmSchedule schedule3 = modelFactory.generatePmSchedule(false, PoP3Date, null);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/pm-isp/schedules").param("date", broadcastDate)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<PmSchedule> pmList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<PmSchedule>>(){});
        
        assertEquals(2, pmList.size());
        assertFalse(pmList.contains(schedule1));
        assertTrue(pmList.contains(schedule2));
        assertTrue(pmList.contains(schedule3));
    }

    @Test
    void postNewSchedule_shouldAddNewScheduleToDb() throws Exception {
        RecipientNumber recipientNumber = modelFactory.generateRecipientNumber();
        SitePOP sitePOP = modelFactory.generateSitePOP();
        String scheduledDate = "12-11-2023";
        PmScheduleRequestDTO newPmScheduleRequest = PmScheduleRequestDTO.builder()
                                                    .popId(sitePOP.getPopId())
                                                    .recipientName(recipientNumber.getUserName())
                                                    .scheduledDate(scheduledDate)
                                                    .build();
        String jsonRequest = objectMapper.writeValueAsString(newPmScheduleRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/pm-isp/schedules").content(jsonRequest).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        List<PmSchedule> savedSchedule = pmScheduleRepository.findAll();
        assertEquals(1, savedSchedule.size());
        assertEquals(newPmScheduleRequest.getRecipientName(), savedSchedule.get(0).getRecipient().getUserName());
    }

    @Test
    void updateRealizationDate_shouldUpdateRealizationDateinPmField() throws Exception {
        Date PoP2Date = sdformat.parse("08-11-2023");
        String scheduleDate = "08-11-2023";
        String realization = "10-11-2023";
        PmSchedule schedule = modelFactory.generatePmSchedule(false, PoP2Date, null);
        PmScheduleRequestDTO updatePmScheduleRequest = PmScheduleRequestDTO.builder()
                                                    .popId(schedule.getSitePop().getPopId())
                                                    .scheduledDate(scheduleDate)
                                                    .realizationDate(realization)
                                                    .build();
        String jsonRequest = objectMapper.writeValueAsString(updatePmScheduleRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/pm-isp/schedules").content(jsonRequest).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
        List<PmSchedule> savedSchedule = pmScheduleRepository.findAll();

        assertEquals(1, savedSchedule.size());
        assertEquals(realization, sdformat.format(savedSchedule.get(0).getRealizationDate()));
        assertTrue(savedSchedule.get(0).isMaintenanceDone());
    }
}
