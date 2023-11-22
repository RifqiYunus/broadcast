package com.iconkalbar.broadcast.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iconkalbar.broadcast.model.BroadcastNumber;
import com.iconkalbar.broadcast.model.RecipientNumber;
import com.iconkalbar.broadcast.model.request.ContactRequestDTO;
import com.iconkalbar.broadcast.repository.BroadcastNumberRepository;
import com.iconkalbar.broadcast.repository.RecipientNumberRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerTest {

    @Autowired
    private BroadcastNumberRepository broadcastNumberRepository;

    @Autowired
    private RecipientNumberRepository recipientNumberRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void cleanUp() {
        broadcastNumberRepository.deleteAllInBatch();
        recipientNumberRepository.deleteAllInBatch();
    }

    @Test
    void fetchAllBroadcastNumber_shouldReturnAllBroadcastContactAvailable() throws Exception {
        
        BroadcastNumber broadcastNumber1 = BroadcastNumber.builder()
                                            .userName("Har Internal")
                                            .waNumber("0838123123123")
                                            .build();
        BroadcastNumber broadcastNumber2 = BroadcastNumber.builder()
                                            .userName("NOC SBU")
                                            .waNumber("0838456456456")
                                            .build();
        broadcastNumberRepository.save(broadcastNumber1);
        broadcastNumberRepository.save(broadcastNumber2);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/contacts/senders")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        List<BroadcastNumber> numberList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<BroadcastNumber>>(){});
        assertEquals(2, numberList.size());
    }

    @Test
    void fetchAllRecipientNumber_shouldReturnAllRecipientContactAvailable() throws Exception {
        
        RecipientNumber recipientNumber = RecipientNumber.builder()
                                            .userName("Serpo Ketapang")
                                            .waNumber("0838123123123")
                                            .build();
        RecipientNumber recipientNumber2 = RecipientNumber.builder()
                                            .userName("Serpo Putussibau")
                                            .waNumber("0838456456456")
                                            .build();
        recipientNumberRepository.save(recipientNumber);
        recipientNumberRepository.save(recipientNumber2);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/contacts/recipients")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        List<RecipientNumber> numberList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<RecipientNumber>>(){});
        assertEquals(2, numberList.size());
    }
    
    @Test
    void postNewBroadcastNumber_shouldSaveContacttoDatabase() throws Exception {
        ContactRequestDTO newContactRequest = ContactRequestDTO.builder()
                                                .userName("Engineer KP")
                                                .contactNumber("08123232323")
                                                .build();
        String jsonRequest = objectMapper.writeValueAsString(newContactRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/contacts/senders").content(jsonRequest).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        List<BroadcastNumber> savedNumbers = broadcastNumberRepository.findAll();
        assertEquals(savedNumbers.get(0).getWaNumber(), newContactRequest.getContactNumber());
    }

    @Test
    void postNewRecipientNumber_shouldSaveContacttoDatabase() throws Exception {
        ContactRequestDTO newContactRequest = ContactRequestDTO.builder()
                                                .userName("Serpo Baru")
                                                .contactNumber("08123232323")
                                                .build();
        String jsonRequest = objectMapper.writeValueAsString(newContactRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/contacts/recipients").content(jsonRequest).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        List<RecipientNumber> savedNumbers = recipientNumberRepository.findAll();
        assertEquals(savedNumbers.get(0).getWaNumber(), newContactRequest.getContactNumber());
    }
}
