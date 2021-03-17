package com.bitforce.tuteme.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String dob;
    private String imageUrl;
    private String city;
    private String district;
    private String level;
    private String bio;
    private String description;
    private Double rating;
}
