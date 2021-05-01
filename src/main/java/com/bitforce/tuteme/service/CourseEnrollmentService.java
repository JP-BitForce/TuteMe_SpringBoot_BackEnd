package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.ServiceRequest.EnrollCourseAndPayRequest;
import com.bitforce.tuteme.dto.ServiceResponse.GetEnrolledCoursesResponse;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.model.*;
import com.bitforce.tuteme.repository.*;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class CourseEnrollmentService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PaymentService paymentService;
    private final CourseService courseService;
    private final ResourceRepository resourceRepository;

    public CourseEnrollmentService(CourseRepository courseRepository,
                                   UserRepository userRepository,
                                   EnrollmentRepository enrollmentRepository,
                                   PaymentService paymentService,
                                   CourseService courseService,
                                   ResourceRepository resourceRepository
    ) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.paymentService = paymentService;
        this.courseService = courseService;
        this.resourceRepository = resourceRepository;
    }

    public String handleEnrollment(EnrollCourseAndPayRequest request) throws EntityNotFoundException {
        Long uId = getLong(request.getUserId());
        User user = getUser(uId);
        Long cId = getLong(request.getCourseId());
        Course course = getCourse(cId);
        Payment payment = paymentService.createNewPayment(request, course, user);
        Enrollment enrollment = Enrollment
                .builder()
                .course(course)
                .payment(payment)
                .user(user)
                .enrolledAt(LocalDateTime.now())
                .build();
        enrollmentRepository.save(enrollment);
        return "course enrolled successfully";
    }

    public GetEnrolledCoursesResponse getCourses(Long userId, int page) throws EntityNotFoundException {
        User user = getUser(userId);
        Page<Enrollment> enrollments = enrollmentRepository.findByUser(user, PageRequest.of(page, 10));
        return new GetEnrolledCoursesResponse(
                enrollments.stream().map(enrollment -> new GetEnrolledCoursesResponse.EnrolledCourse(
                        enrollment.getCourse().getId(),
                        enrollment.getCourse().getName(),
                        enrollment.getCourse().getDescription(),
                        enrollment.getCourse().getDuration(),
                        enrollment.getEnrolledAt(),
                        getUserFullName(enrollment.getCourse().getTutor().getUser()),
                        getCourseImage(enrollment.getCourse().getImageUrl()),
                        enrollment.getCourse().getRating(),
                        enrollment.getCourse().getSchedules(),
                        resourceRepository.findAllByCourseOrderByUploadedDesc(enrollment.getCourse())
                )).collect(Collectors.toList())
        );
    }

    private Long getLong(String id) {
        return Long.parseLong(id);
    }

    private User getUser(Long id) throws EntityNotFoundException {
        if (!userRepository.findById(id).isPresent()) {
            throw new EntityNotFoundException("USER_NOT_FOUND");
        }
        return userRepository.findById(id).get();
    }

    private Course getCourse(Long id) throws EntityNotFoundException {
        if (!courseRepository.findById(id).isPresent()) {
            throw new EntityNotFoundException("COURSE_NOT_FOUND");
        }
        return courseRepository.findById(id).get();
    }

    private String getUserFullName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }

    @SneakyThrows
    private byte[] getCourseImage(String url) {
        if (url != null) {
            String[] filename = url.trim().split("http://localhost:8080/api/courses/uploads/Courses/");
            return courseService.getImageByte(filename[1]);
        }
        return null;
    }
}
