package com.bitforce.tuteme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Double rating;
    private String duration;
    private BigDecimal price;
    private Long tutorId;
    private String tutorName;
    private Long categoryId;
    private String categoryName;
}
