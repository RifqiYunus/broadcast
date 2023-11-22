package com.iconkalbar.broadcast.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NewPmScheduleRequest {

    private int id;
    
    private String popId;

    private String scheduledDate;

    private String recipientName;
        
}
