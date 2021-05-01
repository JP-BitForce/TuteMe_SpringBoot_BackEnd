package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.dto.ApiResponse;
import com.bitforce.tuteme.dto.ControllerRequest.EnrollCourseAndPayControllerRequest;
import com.bitforce.tuteme.dto.ControllerRequest.EnrollCourseByBankControllerRequest;
import com.bitforce.tuteme.dto.ServiceRequest.EnrollCourseAndPayRequest;
import com.bitforce.tuteme.dto.ServiceResponse.GetEnrolledCoursesResponse;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.service.CourseEnrollmentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/courses/enrollment")
public class CourseEnrollmentController {
    private static final Logger log = LoggerFactory.getLogger(CourseEnrollmentController.class);
    private final CourseEnrollmentService courseEnrollmentService;

    @PostMapping(value = "/byBank", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> enrollCourseByBankPay(@RequestPart("request") EnrollCourseByBankControllerRequest request,
                                                   @RequestPart("file") MultipartFile file
    ) {
        try {
            EnrollCourseAndPayRequest enrollCourseAndPayRequest = EnrollCourseAndPayRequest
                    .builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .userId(request.getUserId())
                    .courseId(request.getCourseId())
                    .paymentType(request.getPaymentType())
                    .depositedAt(request.getDepositedAt())
                    .formData(file)
                    .paymentMethod("bank")
                    .amount(request.getAmount())
                    .build();
            String response = courseEnrollmentService.handleEnrollment(enrollCourseAndPayRequest);
            ApiResponse apiResponse = new ApiResponse(true, response);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to enroll course with id: {}", request.getCourseId());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/byPaypal")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> enrollCourseByPaypalPay(@RequestBody EnrollCourseAndPayControllerRequest request) {
        try {
            EnrollCourseAndPayRequest enrollCourseAndPayRequest = EnrollCourseAndPayRequest
                    .builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .address(request.getAddress())
                    .city(request.getCity())
                    .zip(request.getZip())
                    .mobile(request.getMobile())
                    .email(request.getEmail())
                    .cvv(request.getCvv())
                    .exp(request.getExp())
                    .userId(request.getUserId())
                    .courseId(request.getCourseId())
                    .paymentType(request.getPaymentType())
                    .paymentMethod("paypal")
                    .cardNo(request.getCardNo())
                    .amount(request.getAmount())
                    .build();
            String response = courseEnrollmentService.handleEnrollment(enrollCourseAndPayRequest);
            ApiResponse apiResponse = new ApiResponse(true, response);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to enroll course with id: {}", request.getCourseId());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/get_enrolled_courses")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getEnrolledCourses(@RequestParam Long uId, @RequestParam int page) {
        try {
            GetEnrolledCoursesResponse response = courseEnrollmentService.getCourses(uId, page);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            log.error("Unable to get enrolled courses for userId: {}", uId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to get enrolled courses for userId: {}", uId);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
