package com.bitforce.tuteme.dto.ServiceRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddSystemFeedbackRequest {
    private String userId;
    private String profileId;
    private String email;
    private String feedback;
    private Double rating;
    private boolean isServiceFind;
}
