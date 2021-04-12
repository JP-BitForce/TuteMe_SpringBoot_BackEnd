package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.model.CourseEnrollment;
import com.bitforce.tuteme.service.CourseEnrollmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @GetMapping("/{studentId}/{page}")
    public ResponseEntity<Map<String, Object>> getAllEnrolledCoursesForStudent(@PathVariable Long studentId, @PathVariable int page){
        return courseEnrollmentService.getAllEnrolledCoursesForStudent(studentId,page);
    }

    @DeleteMapping("/{enrollmentId}")
    private String unEnrollToCourse(@PathVariable Long enrollmentId){
        return courseEnrollmentService.unEnrollToCourse(enrollmentId);
    }
}
