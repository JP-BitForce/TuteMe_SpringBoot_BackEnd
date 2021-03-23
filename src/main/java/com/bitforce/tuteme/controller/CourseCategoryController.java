package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.model.CourseCategory;
import com.bitforce.tuteme.service.CourseCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/courses/category")
public class CourseCategoryController {

   private final CourseCategoryService courseCategoryService;

    @PostMapping
    public CourseCategory createCategory(@RequestBody CourseCategory courseCategory){
        return courseCategoryService.createCategory(courseCategory);
    }

    @GetMapping
    public List<CourseCategory> getAllCourseCategory(){
        return courseCategoryService.getAllCourseCategory();
    }
}
