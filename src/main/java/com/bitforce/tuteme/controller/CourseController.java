package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.model.Course;
import com.bitforce.tuteme.service.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    public Page<Course> getAllCourses(Pageable pageable){
        return courseService.getAllCourses(pageable);
    }

    @GetMapping("/{courseId}")
    public Optional<Course> getCourse(@PathVariable Long courseId){
        return courseService.getCourse(courseId);
    }

    @PutMapping("/{courseId}")
    public Course updateCourse(@RequestBody Course course ,@PathVariable Long courseId){
        return courseService.updateCourse(course,courseId);
    }

    @DeleteMapping("/{courseId}")
    public String deleteCourse(@PathVariable Long courseId){
        return courseService.deleteCourse(courseId);
    }
}
