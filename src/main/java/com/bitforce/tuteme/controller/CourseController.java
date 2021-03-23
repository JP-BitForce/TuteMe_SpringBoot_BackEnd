package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.model.Course;
import com.bitforce.tuteme.service.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public Course createCategory(@RequestBody Course course){
        return courseService.createCourse(course);
    }

    @GetMapping
    public List<Course> getAllCourses(){
        return courseService.getAllCourses();
    }
}
