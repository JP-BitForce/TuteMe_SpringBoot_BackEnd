package com.bitforce.tuteme.service;

import com.bitforce.tuteme.model.Course;
import com.bitforce.tuteme.repository.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Page<Course> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    public Optional<Course> getCourse(Long courseId) {
        return courseRepository.findById(courseId);
    }

    public Course updateCourse(Course course, Long courseId) {
        Optional<Course> course1 =courseRepository.findById(courseId);
        BeanUtils.copyProperties(course,course1);
        return course;
    }

    public String deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
        return "CourseCategory Deleted Successfully..!";
    }
}
