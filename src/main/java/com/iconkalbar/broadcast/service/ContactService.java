package com.iconkalbar.broadcast.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iconkalbar.broadcast.model.BroadcastNumber;
import com.iconkalbar.broadcast.model.RecipientNumber;
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
}
