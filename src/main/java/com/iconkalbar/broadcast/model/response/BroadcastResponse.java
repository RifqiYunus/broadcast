package com.iconkalbar.broadcast.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BroadcastResponse {
    
    private String status;

    private String msg;
}
