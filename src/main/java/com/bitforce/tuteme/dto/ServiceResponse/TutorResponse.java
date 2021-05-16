package com.bitforce.tuteme.dto.ServiceResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String imageUrl;
    private String city;
    private String district;
    private String description;
    private String facebook;
    private String twitter;
    private String instagram;
    private String linkedIn;
}
