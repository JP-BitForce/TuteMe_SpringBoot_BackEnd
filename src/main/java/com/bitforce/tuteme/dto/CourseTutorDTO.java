package com.bitforce.tuteme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseTutorDTO {
    private Long id;
    private String firstName;
    private String lastName;
}
