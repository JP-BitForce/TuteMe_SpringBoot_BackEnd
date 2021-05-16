package com.bitforce.tuteme.dto.ServiceRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTutorProfileRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String city;
    private String district;
    private String bio;
    private Long userId;
    private String facebook;
    private String twitter;
    private String instagram;
    private String linkedIn;
}
