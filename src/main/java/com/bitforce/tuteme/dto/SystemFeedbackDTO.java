package com.bitforce.tuteme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemFeedbackDTO {
    private Long id;
    private String feedback;
    private Double rating;
    private boolean finding;
    private Long userId;
    private String userName;
    private String userType;
    private byte[] imageUrl;
}
