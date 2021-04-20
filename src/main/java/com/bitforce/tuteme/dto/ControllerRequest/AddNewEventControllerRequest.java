package com.bitforce.tuteme.dto.ControllerRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddNewEventControllerRequest {
    private String userId;
    private String title;
    private String description;
    private String start;
    private String end;
    private String backgroundColor;
}
