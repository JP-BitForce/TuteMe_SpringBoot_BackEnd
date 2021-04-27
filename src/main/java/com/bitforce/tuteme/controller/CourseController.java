package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.dto.ApiResponse;
import com.bitforce.tuteme.dto.ControllerRequest.EnrollCourseAndPayControllerRequest;
import com.bitforce.tuteme.dto.ControllerRequest.FilterCoursesControllerRequest;
import com.bitforce.tuteme.dto.CourseTutorDTO;
import com.bitforce.tuteme.dto.ServiceRequest.EnrollCourseAndPayRequest;
import com.bitforce.tuteme.dto.ServiceRequest.FilterCoursesRequest;
import com.bitforce.tuteme.dto.ServiceResponse.GetFilterCategoriesResponse;
import com.bitforce.tuteme.model.Course;
import com.bitforce.tuteme.service.CourseService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private static final Logger log = LoggerFactory.getLogger(CourseController.class);
    private final CourseService courseService;

    @PostMapping
    public Course createCourse(@RequestParam MultipartFile file, @ModelAttribute Course course) {
        return courseService.createCourse(file, course);
    }

    @GetMapping("getAll/{page}")
    public ResponseEntity<Map<String, Object>> getAllCourses(@PathVariable int page) {
        return courseService.getAllCourses(page);
    }

    @GetMapping("/{courseId}")
    public Optional<Course> getCourse(@PathVariable Long courseId) {
        return courseService.getCourse(courseId);
    }

    @PutMapping("/{courseId}")
    public Course updateCourse(@RequestBody Course course, @PathVariable Long courseId) {
        return courseService.updateCourse(course, courseId);
    }

    @DeleteMapping("/{courseId}")
    public String deleteCourse(@PathVariable Long courseId) {
        return courseService.deleteCourse(courseId);
    }

    @SneakyThrows
    @GetMapping(value = "uploads/courses/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImageResource(@PathVariable String filename) {
        return courseService.getImageByte(filename);
    }

    @GetMapping("/tutors")
    public List<CourseTutorDTO> getAllTutors() {
        return courseService.getAllTutors();
    }

    @GetMapping("/getFilterCategories")
    public GetFilterCategoriesResponse getCourseFilterCategories() {
        try {
            return courseService.getFilterCategories();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/searchCourseByValue")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> searchCourseByValue(@RequestParam int page, @RequestParam String value) {
        try {
            Map<String, Object> response = courseService.searchByValue(page, value);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to search course by value");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/filterCourses")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> filterCourses(@RequestBody FilterCoursesControllerRequest request) {
        try {
            FilterCoursesRequest filterCoursesRequest = new FilterCoursesRequest(
                    request.getCategoryList(),
                    request.getTutorList(),
                    request.getTypeList(),
                    request.getPriceList(),
                    request.getPage()
            );
            Map<String, Object> response = courseService.filterCourses(filterCoursesRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to search course by value");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/course_enrollemnt", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> enrollCourse(@RequestPart("request") EnrollCourseAndPayControllerRequest request, @RequestPart("file") MultipartFile file) {
        try {
            EnrollCourseAndPayRequest enrollCourseAndPayRequest = new EnrollCourseAndPayRequest(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getAddress(),
                    request.getCity(),
                    request.getZip(),
                    request.getMobile(),
                    request.getEmail(),
                    request.getCvv(),
                    request.getExp(),
                    request.getCardNo(),
                    request.getDepositedAt(),
                    file,
                    request.getPaymentMethod(),
                    request.getUserId(),
                    request.getCourseId(),
                    request.getPaymentType()
            );
            String response = courseService.handleEnrollment(enrollCourseAndPayRequest);
            ApiResponse apiResponse = new ApiResponse(true, response);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to search course by value");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
