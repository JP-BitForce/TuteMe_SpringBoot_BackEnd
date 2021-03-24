package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.model.CourseCategory;
import com.bitforce.tuteme.service.CourseCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    public Page<CourseCategory> getAllCourseCategory(Pageable pageable){
        return courseCategoryService.getAllCourseCategory(pageable);
    }

    @GetMapping("/{categoryId}")
    public Optional<CourseCategory> getCourseCategory(@PathVariable Long categoryId){
        return courseCategoryService.getCourseCategory(categoryId);
    }

    @PutMapping("/{categoryId}")
    public CourseCategory updateCourseCategory(@RequestBody CourseCategory courseCategory ,@PathVariable Long categoryId){
       return courseCategoryService.updateCourseCategory(courseCategory,categoryId);
    }

    @DeleteMapping("/{categoryId}")
    public String deleteCourseCategory(@PathVariable Long categoryId){
        return courseCategoryService.deleteCourseCategory(categoryId);
    }

}
