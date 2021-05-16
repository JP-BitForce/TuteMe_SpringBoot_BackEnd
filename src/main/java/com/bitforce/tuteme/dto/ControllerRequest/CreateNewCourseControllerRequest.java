package com.bitforce.tuteme.dto.ControllerRequest;

import com.bitforce.tuteme.model.Schedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewCourseControllerRequest {
    private Long tutorId;
    private String courseName;
    private String description;
    private BigDecimal price;
    private String category;
    private String type;
    private int year;
    private int month;
    private int days;
    private List<Schedule> schedules;
}
