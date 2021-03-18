package com.bitforce.tuteme.dto;

import lombok.Data;

@Data
public class TutorProfileDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String imageUrl;
    private String city;
    private String district;
    private String description;
    private Double rating;
}
