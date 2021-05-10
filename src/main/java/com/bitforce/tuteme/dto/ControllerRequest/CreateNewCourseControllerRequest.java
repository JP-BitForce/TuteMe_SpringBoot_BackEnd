package com.bitforce.tuteme.dto.ControllerRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewCourseControllerRequest {
    private Long tutorId;
    private String fullName;
    private String email;
    private String courseName;
    private String description;
    private BigDecimal price;
    private String category;
    private String type;
    private int year;
    private int month;
    private int days;
}
