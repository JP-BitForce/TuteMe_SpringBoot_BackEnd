package com.bitforce.tuteme.dto.ServiceResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCourseCategoryResponse {
    List<CourseCategory> courseCategoryList = new ArrayList<>();
    private int total;
    private int current;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CourseCategory {
        private Long id;
        private String category;
        private byte[] src;
    }
}
