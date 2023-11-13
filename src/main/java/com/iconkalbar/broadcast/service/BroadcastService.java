package com.iconkalbar.broadcast.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iconkalbar.broadcast.model.BroadcastNumber;
import com.iconkalbar.broadcast.model.PmSchedule;
import com.iconkalbar.broadcast.model.request.BroadcastRequest;

@Service
public class BroadcastService {

    public BroadcastService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;

    public ResponseEntity<String> blastMessage(PmSchedule pmSchedule, BroadcastNumber broadcastNumber) throws RestClientException, JsonProcessingException {
        BroadcastRequest broadcastRequest = BroadcastRequest.builder()
                                            .apiKey(Constants.API_KEY)
                                            .sender(broadcastNumber.getWaNumber())
                                            .number(pmSchedule.getRecipient().getWaNumber())
                                            .message("Hello World").build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<String>(objectMapper.writeValueAsString(broadcastRequest), headers);
        ResponseEntity<String> bResponseEntity = restTemplate.postForEntity(Constants.BLAST_URI, requestEntity, String.class);
        return bResponseEntity;
    }
}
