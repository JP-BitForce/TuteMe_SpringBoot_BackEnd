package com.bitforce.tuteme.dto.ServiceResponse;

import com.bitforce.tuteme.model.Resource;
import com.bitforce.tuteme.model.Schedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetEnrolledCoursesResponse {
    List<EnrolledCourse> enrolledCourses = new ArrayList<>();
    private int total;
    private int current;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnrolledCourse {
        private Long id;
        private String title;
        private String description;
        private String duration;
        private LocalDateTime enrolledAt;
        private String tutorName;
        private byte[] courseImg;
        private double rating;
        private List<Schedule> schedules;
        private List<Resource> resources;
        private Long tutorId;
    }
}
