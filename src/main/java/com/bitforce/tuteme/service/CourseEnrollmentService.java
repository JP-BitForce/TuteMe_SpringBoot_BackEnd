package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.ServiceRequest.EnrollCourseAndPayRequest;
import com.bitforce.tuteme.dto.ServiceResponse.GetEnrolledCoursesResponse;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.model.*;
import com.bitforce.tuteme.repository.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseEnrollmentService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PaymentService paymentService;
    private final CourseService courseService;
    private final CourseEnrollmentDetailRepository courseEnrollmentDetailRepository;
    private final ResourceRepository resourceRepository;

    public CourseEnrollmentService(CourseRepository courseRepository,
                                   UserRepository userRepository,
                                   EnrollmentRepository enrollmentRepository,
                                   PaymentService paymentService,
                                   CourseService courseService,
                                   CourseEnrollmentDetailRepository courseEnrollmentDetailRepository,
                                   ResourceRepository resourceRepository
    ) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.paymentService = paymentService;
        this.courseService = courseService;
        this.courseEnrollmentDetailRepository = courseEnrollmentDetailRepository;
        this.resourceRepository = resourceRepository;
    }

    public String handleEnrollment(EnrollCourseAndPayRequest request) throws EntityNotFoundException {
        Long uId = getLong(request.getUserId());
        User user = getUser(uId);
        Long cId = getLong(request.getCourseId());
        Course course = getCourse(cId);
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
        Payment payment = paymentService.createNewPayment(request);
        CourseEnrollmentDetail courseEnrollmentDetail = CourseEnrollmentDetail
                .builder()
                .enrollmentDate(LocalDateTime.now())
                .course(course)
                .payment(payment)
                .build();
        courseEnrollmentDetailRepository.save(courseEnrollmentDetail);
        return "course enrolled successfully";
    }

    public GetEnrolledCoursesResponse getCourses(Long userId) throws EntityNotFoundException {
        Enrollment enrollment = getEnrollmentByUser(userId);
        if (enrollment != null) {
            List<Course> courses = enrollment.getCourses();
            return new GetEnrolledCoursesResponse(
                    courses.stream().map(course -> new GetEnrolledCoursesResponse.EnrolledCourse(
                            course.getId(),
                            course.getName(),
                            course.getDescription(),
                            course.getDuration(),
                            courseEnrollmentDetailRepository.findByCourse(course).getEnrollmentDate(),
                            getUserFullName(course.getTutor().getUser()),
                            getCourseImage(course.getImageUrl()),
                            course.getRating(),
                            course.getSchedules(),
                            resourceRepository.findAllByCourseOrderByUploadedDesc(course)
                    )).collect(Collectors.toList())
            );
        }
        return new GetEnrolledCoursesResponse(new ArrayList<>());
    }

    public Enrollment getEnrollmentByUser(Long id) throws EntityNotFoundException {
        User user = getUser(id);
        return enrollmentRepository.findByUser(user);
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
