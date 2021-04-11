package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.dto.CourseTutorDTO;
import com.bitforce.tuteme.model.Course;
import com.bitforce.tuteme.service.CourseService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public Course createCourse(@RequestParam MultipartFile file, @ModelAttribute Course course){
        return courseService.createCourse(file,course);
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

    @SneakyThrows
    @GetMapping(value = "uploads/courses/{filename}",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImageResource(@PathVariable String filename) {
        return courseService.getImageByte(filename);
    }

    @GetMapping("/tutors")
    public List<CourseTutorDTO> getAllTutors(){
        return courseService.getAllTutors();
    }
}
