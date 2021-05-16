package com.bitforce.tuteme.dto.ServiceResponse;

import com.bitforce.tuteme.model.CourseCategory;
import com.bitforce.tuteme.model.CourseDuration;
import com.bitforce.tuteme.model.CourseType;
import com.bitforce.tuteme.model.Schedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCourseByTutorResponse {
    private Long id;
    private String title;
    private String description;
    private byte[] courseImg;
    private BigDecimal price;
    private CourseCategory courseCategory;
    private CourseType courseType;
    private List<Schedule> schedules;
    private CourseDuration courseDuration;
}
