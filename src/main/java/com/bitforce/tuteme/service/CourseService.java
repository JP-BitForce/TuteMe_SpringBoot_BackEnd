package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.CourseTutorDTO;
import com.bitforce.tuteme.model.Course;
import com.bitforce.tuteme.repository.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

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

    public ResponseEntity<Map<String, Object>>  getAllCourses(int page) {
        try {
            Pageable paging = PageRequest.of(page, 10);
            Page<Course> coursePage = courseRepository.findAll(paging);
            List<Course> courses = coursePage.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("data", courses);
            response.put("currentPage", coursePage.getNumber());
            response.put("totalPages", coursePage.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    public byte[] getImageByte(String filename) throws IOException {
        return fileStorageService.convert(filename);
    }

    public List<CourseTutorDTO> getAllTutors() {
        List<CourseTutorDTO> courseTutorDTOS = new ArrayList<>();
        List<Course> courses = courseRepository.findAll();
        for(Course course : courses){
            CourseTutorDTO courseTutorDTO = new CourseTutorDTO();
            courseTutorDTO.setId(course.getTutor().getId());
            courseTutorDTO.setFirstName(course.getTutor().getUser().getFirstName());
            courseTutorDTO.setLastName(course.getTutor().getUser().getLastName());

            courseTutorDTOS.add(courseTutorDTO);
        }

        return courseTutorDTOS;
    }
}
