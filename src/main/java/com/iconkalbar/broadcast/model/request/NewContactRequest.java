package com.iconkalbar.broadcast.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NewContactRequest {
    
    private String userName;

    private String contactNumber;

}
