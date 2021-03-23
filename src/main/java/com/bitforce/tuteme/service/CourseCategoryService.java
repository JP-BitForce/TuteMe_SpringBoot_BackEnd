package com.bitforce.tuteme.service;

import com.bitforce.tuteme.model.CourseCategory;
import com.bitforce.tuteme.repository.CourseCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseCategoryService {

    private CourseCategoryRepository courseCategoryRepository;

    public  CourseCategory createCategory(CourseCategory courseCategoryDTO) {
        return courseCategoryRepository.save(courseCategoryDTO);
    }

    public List<CourseCategory> getAllCourseCategory() {
        return courseCategoryRepository.findAll();
    }
}
