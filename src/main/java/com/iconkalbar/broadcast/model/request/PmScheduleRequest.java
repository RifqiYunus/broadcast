package com.iconkalbar.broadcast.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PmScheduleRequest {

    private int id;
    
    private String popId;

    private String scheduledDate;

    private String recipientName;
        
    private String realizationDate;

    private boolean isMaintenanceDone; 
}
