package com.iconkalbar.broadcast.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BroadcastRequestDTO {
    
    @JsonProperty("api_key")
    private String apiKey;

    private String sender;

    private String number;

    private String message;
    
}
