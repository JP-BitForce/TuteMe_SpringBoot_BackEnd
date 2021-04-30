package com.bitforce.tuteme.dto;

import com.bitforce.tuteme.model.Schedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private Long id;
    private String name;
    private String description;
    private byte[] imageUrl;
    private Double rating;
    private String duration;
    private BigDecimal price;
    private Long tutorId;
    private String tutorName;
    private Long categoryId;
    private String categoryName;
    private boolean isEnrolledByCurrentUser;
    private List<Schedule> schedules;
}
