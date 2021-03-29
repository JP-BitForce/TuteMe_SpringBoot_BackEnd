package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.model.CourseEnrollment;
import com.bitforce.tuteme.service.CourseEnrollmentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/courses/enrollment")
public class CourseEnrollmentController {
    private final CourseEnrollmentService courseEnrollmentService;

    @PostMapping
    public CourseEnrollment enrollToCourse(@RequestParam Long courseId, @RequestParam Long studentId ){
        return courseEnrollmentService.enrollToCourse(courseId,studentId);
    }

    @GetMapping("/{studentId}")
    public Page<CourseEnrollment> getAllEnrolledCoursesForStudent(@PathVariable Long studentId, Pageable pageable){
        return courseEnrollmentService.getAllEnrolledCoursesForStudent(studentId,pageable);
    }

    @DeleteMapping("/{enrollmentId}")
    private String unEnrollToCourse(@PathVariable Long enrollmentId){
        return courseEnrollmentService.unEnrollToCourse(enrollmentId);
    }
}
