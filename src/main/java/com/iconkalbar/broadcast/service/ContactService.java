package com.iconkalbar.broadcast.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iconkalbar.broadcast.model.BroadcastNumber;
import com.iconkalbar.broadcast.model.RecipientNumber;
import com.iconkalbar.broadcast.model.request.ContactRequestDTO;
import com.iconkalbar.broadcast.repository.BroadcastNumberRepository;
import com.iconkalbar.broadcast.repository.RecipientNumberRepository;

@Service
public class ContactService {
    
    @Autowired
    private BroadcastNumberRepository broadcastNumberRepository;

    @Autowired
    private RecipientNumberRepository recipientNumberRepository;

    public List<BroadcastNumber> getAllBroadcastNumber(){
        return broadcastNumberRepository.findAll();
    }

    public List<RecipientNumber> getAllRecipientNumber(){
        return recipientNumberRepository.findAll();
    }

    public List<BroadcastNumber> getBySenderName(String name){
        return broadcastNumberRepository.findByUserName(name);
    }

    public List<RecipientNumber> getByRecipientName(String name){
        return recipientNumberRepository.findByUserName(name);
    }

    public ContactRequestDTO saveBroadcastNumber(ContactRequestDTO contactRequest) {
        BroadcastNumber broadcastNumber = BroadcastNumber.builder()
                                            .userName(contactRequest.getUserName())
                                            .waNumber(contactRequest.getContactNumber())
                                            .build();
        BroadcastNumber savedNumber = broadcastNumberRepository.save(broadcastNumber);
        return new ContactRequestDTO(savedNumber.getId() ,savedNumber.getUserName(), savedNumber.getWaNumber(), "Sender");
    }

    public ContactRequestDTO saveRecipientNumber(ContactRequestDTO contactRequest) {
        RecipientNumber recipientNumber = RecipientNumber.builder()
                                            .userName(contactRequest.getUserName())
                                            .waNumber(contactRequest.getContactNumber())
                                            .build();
        RecipientNumber savedNumber = recipientNumberRepository.save(recipientNumber);
        return new ContactRequestDTO(savedNumber.getId() ,savedNumber.getUserName(), savedNumber.getWaNumber(), "Recipient");
    }
}
