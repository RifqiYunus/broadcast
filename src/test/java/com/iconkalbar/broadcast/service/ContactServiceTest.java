package com.iconkalbar.broadcast.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.iconkalbar.broadcast.model.BroadcastNumber;
import com.iconkalbar.broadcast.model.RecipientNumber;
import com.iconkalbar.broadcast.repository.BroadcastNumberRepository;
import com.iconkalbar.broadcast.repository.RecipientNumberRepository;

@SpringBootTest
public class ContactServiceTest {
    
    @Autowired
    private BroadcastNumberRepository broadcastNumberRepository;

    @Autowired
    private RecipientNumberRepository recipientNumberRepository;

    @Autowired
    private ContactService contactService;

    @AfterEach
    void cleanup() {
        broadcastNumberRepository.deleteAllInBatch();
        recipientNumberRepository.deleteAllInBatch();
    }

    @Test
    void getAllBroadcastNumberShouldCallBroadcastRepositoryFindAll() {

        BroadcastNumber broadcastNumber1 = BroadcastNumber.builder()
                                            .userName("NOC SBU")
                                            .waNumber("081231231232")
                                            .build();
        broadcastNumberRepository.save(broadcastNumber1);
        BroadcastNumber broadcastNumber2 = BroadcastNumber.builder()
                                            .userName("Teknisi Har Internal")
                                            .waNumber("081456456456")
                                            .build();
        broadcastNumberRepository.save(broadcastNumber2);

        List<BroadcastNumber> numberList = contactService.getAllBroadcastNumber();

        assertEquals(2, numberList.size());
    }

    @Test
    void getBroadcastNumberBySenderNameShouldRetrieveOneContact() {

        BroadcastNumber broadcastNumber1 = BroadcastNumber.builder()
                                            .userName("NOC SBU")
                                            .waNumber("081231231232")
                                            .build();
        broadcastNumberRepository.save(broadcastNumber1);
        BroadcastNumber broadcastNumber2 = BroadcastNumber.builder()
                                            .userName("Teknisi Har Internal")
                                            .waNumber("081456456456")
                                            .build();
        broadcastNumberRepository.save(broadcastNumber2);

        List<BroadcastNumber> numberList = contactService.getBySenderName("NOC SBU");

        assertEquals(1, numberList.size());
        assertEquals(broadcastNumber1, numberList.get(0));
    }    

    @Test
    void getAllRecipientNumberShouldCallRecipientRepositoryFindAll() {
        RecipientNumber recipientNumber1 = RecipientNumber.builder()
                                            .userName("Serpo Pontianak")
                                            .waNumber("081231231232")
                                            .build();
        recipientNumberRepository.save(recipientNumber1);
        RecipientNumber recipientNumber2 = RecipientNumber.builder()
                                            .userName("Teknisi Har Internal")
                                            .waNumber("081456456456")
                                            .build();
        recipientNumberRepository.save(recipientNumber2);

        List<RecipientNumber> numberList = contactService.getAllRecipientNumber();

        assertEquals(2, numberList.size());
    }

    @Test
    void getRecipientNumberByRecipientNumberShouldRetrieveOneContact() {
        RecipientNumber recipientNumber1 = RecipientNumber.builder()
                                            .userName("Serpo Pontianak")
                                            .waNumber("081231231232")
                                            .build();
        recipientNumberRepository.save(recipientNumber1);
        RecipientNumber recipientNumber2 = RecipientNumber.builder()
                                            .userName("Teknisi Har Internal")
                                            .waNumber("081456456456")
                                            .build();
        recipientNumberRepository.save(recipientNumber2);

        List<RecipientNumber> numberList = contactService.getByRecipientName("Serpo Pontianak");

        assertEquals(1, numberList.size());
        assertEquals(recipientNumber1, numberList.get(0));
    }

    @Test
    void saveBroadcastNumberShouldSaveNewObjectToDatabase() {
        BroadcastNumber broadcastNumber1 = BroadcastNumber.builder()
                                            .userName("NOC SBU")
                                            .waNumber("081231231232")
                                            .build();
        
        contactService.saveBroadcastNumber(broadcastNumber1);
        
        List<BroadcastNumber> savedBroadcastNumber = broadcastNumberRepository.findAll();
        assertEquals(1, savedBroadcastNumber.size());
    }

    @Test
    void saveRecipientNumberShouldSaveNewObjectToDatabase() {
        RecipientNumber recipientNumber = RecipientNumber.builder()
                                            .userName("NOC SBU")
                                            .waNumber("081231231232")
                                            .build();
        
        contactService.saveRecipientNumber(recipientNumber);
        
        List<RecipientNumber> savedRecipientNumbers = recipientNumberRepository.findAll();
        assertEquals(1, savedRecipientNumbers.size());
    }
}
