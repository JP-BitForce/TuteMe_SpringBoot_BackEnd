package com.bitforce.tuteme.dto.ControllerRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterEnrolledCoursesControllerRequest {
    private int page;
    private List<String> courseCategories;
    private Long userId;
}
