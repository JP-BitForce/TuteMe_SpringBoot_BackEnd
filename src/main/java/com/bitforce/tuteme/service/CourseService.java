package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.CourseDTO;
import com.bitforce.tuteme.dto.CourseTutorDTO;
import com.bitforce.tuteme.model.Course;
import com.bitforce.tuteme.repository.CourseRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
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

            List<CourseDTO>  courseDTOS = new ArrayList<>();
            for (Course course : courses){
                CourseDTO courseDTO = new CourseDTO();
                BeanUtils.copyProperties(course,courseDTO);
                courseDTO.setCategoryId(course.getCourseCategory().getId());
                courseDTO.setCategoryName(course.getCourseCategory().getCategory());
                courseDTO.setTutorId(course.getTutor().getId());
                courseDTO.setTutorName(course.getTutor().getUser().getFirstName() +" "+course.getTutor().getUser().getLastName());
                courseDTOS.add(courseDTO);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("data", courseDTOS);
            response.put("current", coursePage.getNumber());
            response.put("total", coursePage.getTotalPages());

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

    public GetFilterCategoriesResponse getFilterCategories() {
        List<CourseCategory> categoryList = courseCategoryService.getCourseCategories();
        List<String> courseCategoryList = new ArrayList<>();
        for (CourseCategory courseCategory: categoryList) {
            courseCategoryList.add(courseCategory.getCategory());
        }

        List<CourseLevel> levelList = courseLevelRespository.findAll();
        List<String> courseLevelList = new ArrayList<>();
        for(CourseLevel courseLevel: levelList) {
            courseLevelList.add(courseLevel.getCategory());
        }

        List<CoursePriceCategory> priceCategoryList = coursePriceCategoryRepository.findAll();
        List<Course> courses = courseRepository.findAll();
        List<String> tutorList = new ArrayList<>();
        for (Course course : courses) {
            tutorList.add(getUserFullName(course.getTutor().getUser()));
        }
        return new GetFilterCategoriesResponse(
                courseCategoryList,
                tutorList.stream().distinct().collect(Collectors.toList()),
                courseLevelList,
                priceCategoryList
        );
    }

    public String getUserFullName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}
