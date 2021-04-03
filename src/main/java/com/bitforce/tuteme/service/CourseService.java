package com.bitforce.tuteme.service;

import com.bitforce.tuteme.model.Course;
import com.bitforce.tuteme.repository.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final FileStorageService fileStorageService = new FileStorageService("Courses");

    public Course createCourse(MultipartFile file,Course course) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/courses/uploads/courses/")
                .path(fileName)
                .toUriString();

        Course course1 = new Course();
        course.setImageUrl(fileDownloadUri);
        BeanUtils.copyProperties(course,course1);
        return courseRepository.save(course);
    }

    public Page<Course> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    public Optional<Course> getCourse(Long courseId) {
        return courseRepository.findById(courseId);
    }

    public Course updateCourse(Course course, Long courseId) {
        Course course1 =courseRepository.findById(courseId).get();
        course.setImageUrl(course1.getImageUrl());
        BeanUtils.copyProperties(course,course1);
        return course;
    }

    public String deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
        return "CourseCategory Deleted Successfully..!";
    }

    public ResponseEntity<Resource> getImageResource(String filename, HttpServletRequest request) {
        return fileStorageService.loadFileAsResource(filename,request);
    }
}
