package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.CourseDTO;
import com.bitforce.tuteme.dto.CourseTutorDTO;
import com.bitforce.tuteme.dto.ServiceRequest.EnrollCourseAndPayRequest;
import com.bitforce.tuteme.dto.ServiceRequest.FilterCoursesRequest;
import com.bitforce.tuteme.dto.ServiceResponse.GetFilterCategoriesResponse;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.model.*;
import com.bitforce.tuteme.repository.*;
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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final FileStorageService fileStorageService = new FileStorageService("Courses");
    private final CourseCategoryService courseCategoryService;
    private final CourseLevelRespository courseLevelRespository;
    private final CoursePriceCategoryRepository coursePriceCategoryRepository;
    private final TutorProfileService tutorProfileService;
    private final CourseTypeRepository courseTypeRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PaymentService paymentService;
    private final UserRepository userRepository;

    public Course createCourse(MultipartFile file, Course course) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/courses/uploads/courses/")
                .path(fileName)
                .toUriString();

        Course course1 = new Course();
        course.setImageUrl(fileDownloadUri);
        BeanUtils.copyProperties(course, course1);
        return courseRepository.save(course);
    }

    public ResponseEntity<Map<String, Object>> getAllCourses(int page) {
        try {
            Pageable paging = PageRequest.of(page, 10);
            Page<Course> coursePage = courseRepository.findAll(paging);
            List<Course> courses = coursePage.getContent();

            List<CourseDTO> courseDTOS = new ArrayList<>();
            for (Course course : courses) {
                CourseDTO courseDTO = new CourseDTO();
                BeanUtils.copyProperties(course, courseDTO);
                courseDTO.setCategoryId(course.getCourseCategory().getId());
                courseDTO.setCategoryName(course.getCourseCategory().getCategory());
                courseDTO.setTutorId(course.getTutor().getId());
                courseDTO.setTutorName(course.getTutor().getUser().getFirstName() + " " + course.getTutor().getUser().getLastName());
                courseDTOS.add(courseDTO);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("data", courseDTOS);
            response.put("current", coursePage.getNumber());
            response.put("total", coursePage.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Optional<Course> getCourse(Long courseId) {
        return courseRepository.findById(courseId);
    }

    public Course updateCourse(Course course, Long courseId) {
        Course course1 = courseRepository.findById(courseId).get();
        course.setImageUrl(course1.getImageUrl());
        BeanUtils.copyProperties(course, course1);
        return course;
    }

    public String deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
        return "CourseCategory Deleted Successfully..!";
    }

    public List<CourseTutorDTO> getAllTutors() {
        List<CourseTutorDTO> courseTutorDTOS = new ArrayList<>();
        List<Course> courses = courseRepository.findAll();
        for (Course course : courses) {
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
        for (CourseCategory courseCategory : categoryList) {
            courseCategoryList.add(courseCategory.getCategory());
        }

        List<CourseLevel> levelList = courseLevelRespository.findAll();
        List<String> courseLevelList = new ArrayList<>();
        for (CourseLevel courseLevel : levelList) {
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

    public Map<String, Object> searchByValue(int page, String value) {
        Page<Course> coursePage = courseRepository.findAllByNameContaining(
                value,
                PageRequest.of(page, 10)
        );
        return getCoursesResponse(coursePage);
    }

    public Map<String, Object> filterCourses(FilterCoursesRequest request) {
        List<CourseCategory> categoryList = courseCategoryService.getCourseCategoryByName(request.getCategoryList());
        List<Tutor> tutorList = tutorProfileService.getTutorsByName(request.getTutorList());
        List<CourseType> courseTypeList = courseTypeRepository.findAllByTitleIn(request.getTypeList());
        Page<Course> coursePage = courseRepository.findAllByCourseCategoryInAndTutorInAndCourseTypeInAndCoursePriceCategoryIn(
                categoryList,
                tutorList,
                courseTypeList,
                request.getPriceList(),
                PageRequest.of(request.getPage(), 10)
        );
        return getCoursesResponse(coursePage);
    }

    public String handleEnrollment(EnrollCourseAndPayRequest request) throws EntityNotFoundException {
        Long uId = Long.parseLong(request.getUserId());
        if (!userRepository.findById(uId).isPresent()) {
            throw new EntityNotFoundException("USER_NOT_FOUND");
        }
        User user = userRepository.findById(uId).get();
        Long cId = Long.parseLong(request.getCourseId());
        if (!courseRepository.findById(cId).isPresent()) {
            throw new EntityNotFoundException("COURSE_NOT_FOUND");
        }
        Course course = courseRepository.findById(cId).get();
        Enrollment enrollment = enrollmentRepository.findByUser(user);
        List<Course> courses = new ArrayList<>();
        if (enrollment == null) {
            courses.add(course);
            enrollment = Enrollment
                    .builder()
                    .user(user)
                    .courses(courses)
                    .build();
        } else {
            courses = enrollment.getCourses();
            courses.add(course);
            enrollment.setCourses(courses);
        }
        enrollmentRepository.save(enrollment);
        paymentService.createNewPayment(request);
        return "course enrolled successfully";
    }

    public Map<String, Object> getCoursesResponse(Page<Course> coursePage) {
        List<Course> courseList = coursePage.getContent();

        List<CourseDTO> courseDTOS = new ArrayList<>();
        for (Course course : courseList) {
            CourseDTO courseDTO = new CourseDTO();
            BeanUtils.copyProperties(course, courseDTO);
            courseDTO.setCategoryId(course.getCourseCategory().getId());
            courseDTO.setCategoryName(course.getCourseCategory().getCategory());
            courseDTO.setTutorId(course.getTutor().getId());
            courseDTO.setTutorName(course.getTutor().getUser().getFirstName() + " " + course.getTutor().getUser().getLastName());
            courseDTOS.add(courseDTO);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("data", courseDTOS);
        response.put("current", coursePage.getNumber());
        response.put("total", coursePage.getTotalPages());
        return response;
    }

    public String getUserFullName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }

    public ResponseEntity<Resource> getImageResource(String filename, HttpServletRequest request) {
        return fileStorageService.loadFileAsResource(filename, request);
    }

    public byte[] getImageByte(String filename) throws IOException {
        return fileStorageService.convert(filename);
    }

}
