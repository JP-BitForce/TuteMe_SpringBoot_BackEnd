package com.bitforce.tuteme.dto.ServiceResponse;

import com.bitforce.tuteme.model.CourseCategory;
import com.bitforce.tuteme.model.CourseDuration;
import com.bitforce.tuteme.model.CourseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCoursesForPublicResponse {
    List<Course> data = new ArrayList<>();
    private int total;
    private int current;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Course {
        private Long id;
        private String title;
        private String description;
        private byte[] courseImg;
        private BigDecimal price;
        private CourseCategory courseCategory;
        private CourseType courseType;
        private CourseDuration courseDuration;
        private String tutor;
        private double rating;
    }
}
