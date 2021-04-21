package com.bitforce.tuteme.dto.ServiceRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {
    private String userId;
    private String eventId;
    private String title;
    private String description;
    private String start;
    private String end;
    private String backgroundColor;
}
