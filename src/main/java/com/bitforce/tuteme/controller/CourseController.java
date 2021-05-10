package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.dto.ApiResponse;
import com.bitforce.tuteme.dto.ControllerRequest.CreateNewCourseControllerRequest;
import com.bitforce.tuteme.dto.ControllerRequest.FilterCoursesControllerRequest;
import com.bitforce.tuteme.dto.ControllerRequest.SearchCourseControllerRequest;
import com.bitforce.tuteme.dto.CourseTutorDTO;
import com.bitforce.tuteme.dto.ServiceRequest.CreateNewCourseRequest;
import com.bitforce.tuteme.dto.ServiceRequest.FilterCoursesRequest;
import com.bitforce.tuteme.dto.ServiceResponse.GetCourseByIdResponse;
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

    @PostMapping(value = "/createNew", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createCourse(@RequestPart("course") CreateNewCourseControllerRequest request, @RequestPart("file") MultipartFile file) {
        try {
            CreateNewCourseRequest createNewCourseRequest = new CreateNewCourseRequest(
                    request.getTutorId(),
                    request.getFullName(),
                    request.getEmail(),
                    request.getCourseName(),
                    request.getDescription(),
                    request.getPrice(),
                    request.getCategory(),
                    request.getType(),
                    request.getYear(),
                    request.getMonth(),
                    request.getDays()
            );
            String response = courseService.createCourse(file, createNewCourseRequest);
            ApiResponse apiResponse = new ApiResponse(true, response);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("getAll")
    public ResponseEntity<Map<String, Object>> getAllCourses(@RequestParam int page, @RequestParam Long userId) {
        return courseService.getAllCourses(page, userId);
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

    @PostMapping(value = "/searchCourseByValue")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> searchCourseByValue(@RequestBody SearchCourseControllerRequest request) {
        try {
            Map<String, Object> response = courseService.searchByValue(
                    request.getPage(),
                    request.getValue(),
                    request.getUserId()
            );
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
                    request.getPage(),
                    request.getUserId()
            );
            Map<String, Object> response = courseService.filterCourses(filterCoursesRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to search course by value");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/get_course_by_id/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public GetCourseByIdResponse getCourseById(@PathVariable Long id) {
        try {
            return courseService.getCourseById(id);
        } catch (Exception e) {
            log.error("Unable to get course by id: {}", id);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
