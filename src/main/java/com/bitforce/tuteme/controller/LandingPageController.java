package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.dto.ApiResponse;
import com.bitforce.tuteme.dto.ControllerRequest.SendSystemMessageControllerRequest;
import com.bitforce.tuteme.dto.ServiceResponse.GetCoursesForPublicResponse;
import com.bitforce.tuteme.dto.ServiceResponse.GetLandingPageContentsResponse;
import com.bitforce.tuteme.dto.ServiceResponse.GetTutorsResponse;
import com.bitforce.tuteme.exception.AlreadyExistException;
import com.bitforce.tuteme.service.CourseService;
import com.bitforce.tuteme.service.LandingPageService;
import com.bitforce.tuteme.service.SystemFeedbackService;
import com.bitforce.tuteme.service.TutorProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/landingPage")
public class LandingPageController {
    private static final Logger log = LoggerFactory.getLogger(LandingPageController.class);

    private final SystemFeedbackService systemFeedbackService;
    private final CourseService courseService;
    private final TutorProfileService tutorProfileService;
    private final LandingPageService landingPageService;

    public LandingPageController(SystemFeedbackService systemFeedbackService,
                                 CourseService courseService,
                                 TutorProfileService tutorProfileService,
                                 LandingPageService landingPageService
    ) {
        this.systemFeedbackService = systemFeedbackService;
        this.courseService = courseService;
        this.tutorProfileService = tutorProfileService;
        this.landingPageService = landingPageService;
    }

    @GetMapping("/feedback/{page}")
    public ResponseEntity<Map<String, Object>> getAllFeedbacks(@PathVariable int page) {
        try {
            Map<String, Object> response = systemFeedbackService.getAllFeedbacks(page);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to get feedbacks");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/get_courses/{page}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getCourses(@PathVariable int page) {
        try {
            GetCoursesForPublicResponse response = courseService.getCoursesForPublic(page);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to get courses for public");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/search_course")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> searchCourses(@RequestParam int page, @RequestParam String value) {
        try {
            GetCoursesForPublicResponse response = courseService.searchCoursesForPublic(page, value);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to search course for public");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/get_tutors/{page}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getTutors(@PathVariable int page) {
        try {
            GetTutorsResponse response = tutorProfileService.getTutorProfiles(page);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to get tutors for public");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/search_tutor")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> searchTutor(@RequestParam int page, @RequestParam String value) {
        try {
            GetTutorsResponse response = tutorProfileService.searchTutorByValue(value, page);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to search tutor for public");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/send_message")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> sendMessage(@RequestBody SendSystemMessageControllerRequest request) {
        try {
            String response = landingPageService.sendSystemMessage(
                    request.getName(),
                    request.getEmail(),
                    request.getMessage()
            );
            ApiResponse apiResponse = new ApiResponse(true, response);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to send message to system");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/get_landing_page_contents")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getLandingContents() {
        try {
            GetLandingPageContentsResponse response = landingPageService.getLandingPageContents();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to get contents for landing page");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/subscribe_to_newsletter/{email}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> subscribe(@PathVariable String email) {
        try {
            String response = landingPageService.subscribe(email);
            ApiResponse apiResponse = new ApiResponse(true, response);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (AlreadyExistException e) {
            log.error("Unable to subscribe to system, already exist");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already Subscription is made for this email");
        } catch (Exception e) {
            log.error("Unable to subscribe to system");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again later");
        }
    }
}
