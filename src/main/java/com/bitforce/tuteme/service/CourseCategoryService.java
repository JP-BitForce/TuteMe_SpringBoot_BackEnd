package com.bitforce.tuteme.service;

import com.bitforce.tuteme.model.CourseCategory;
import com.bitforce.tuteme.repository.CourseCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseCategoryService {

    private CourseCategoryRepository courseCategoryRepository;

    public  CourseCategory createCategory(CourseCategory courseCategoryDTO) {
        return courseCategoryRepository.save(courseCategoryDTO);
    }

    public Page<CourseCategory> getAllCourseCategory(Pageable pageable) {
        return courseCategoryRepository.findAll(pageable);
    }

    public Optional<CourseCategory> getCourseCategory(Long categoryId) {
        return courseCategoryRepository.findById(categoryId);
    }

    public CourseCategory updateCourseCategory(CourseCategory courseCategory, Long categoryId) {
        Optional<CourseCategory> category =courseCategoryRepository.findById(categoryId);
        BeanUtils.copyProperties(courseCategory,category);
       return courseCategory;
    }

    public String deleteCourseCategory(Long categoryId) {
        courseCategoryRepository.deleteById(categoryId);
        return "CourseCategory Deleted Successfully..!";
    }
}
