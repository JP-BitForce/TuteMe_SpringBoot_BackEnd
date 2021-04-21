package com.bitforce.tuteme.dto.ControllerRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventControllerRequest {
    private String userId;
    private String eventId;
    private String title;
    private String description;
    private String start;
    private String end;
    private String backgroundColor;
}
