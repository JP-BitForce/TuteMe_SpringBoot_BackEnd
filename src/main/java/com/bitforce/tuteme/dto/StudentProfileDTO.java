package com.bitforce.tuteme.dto;

import lombok.Data;

@Data
public class StudentProfileDTO {
    private Long id;
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
    private int courseCount;
    private int tutorCount;
}
