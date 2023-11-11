package com.iconkalbar.broadcast.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iconkalbar.broadcast.model.BroadcastNumber;
import com.iconkalbar.broadcast.model.RecipientNumber;
import com.iconkalbar.broadcast.model.request.NewContactRequest;
import com.iconkalbar.broadcast.service.ContactService;

@RestController
@RequestMapping("contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/senders")
    List<BroadcastNumber> fetchAllBroadcastNumber() {
        return contactService.getAllBroadcastNumber();
    }

    @GetMapping("/recipients")
    List<RecipientNumber> fetchAllRecipientNumber() {
        return contactService.getAllRecipientNumber();
    }
    
    @PostMapping("/senders")
    ResponseEntity<NewContactRequest> postNewSenderContacts(@RequestBody NewContactRequest request) {
        return new ResponseEntity<>(contactService.saveBroadcastNumber(request), HttpStatus.CREATED);
    }

    @PostMapping("/recipients")
    ResponseEntity<NewContactRequest> postNewRecipientContacts(@RequestBody NewContactRequest request) {
        return new ResponseEntity<>(contactService.saveRecipientNumber(request), HttpStatus.CREATED);
    }

}
