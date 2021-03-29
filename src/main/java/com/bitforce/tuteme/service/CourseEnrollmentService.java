package com.bitforce.tuteme.service;

import com.bitforce.tuteme.model.CourseEnrollment;
import com.bitforce.tuteme.repository.CourseEnrollmentRepository;
import com.bitforce.tuteme.repository.CourseRepository;
import com.bitforce.tuteme.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CourseEnrollmentService {

    private final CourseEnrollmentRepository courseEnrollmentRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public CourseEnrollment enrollToCourse(Long courseId, Long studentId) {
        CourseEnrollment courseEnrollment = new CourseEnrollment();
        courseEnrollment.setCourse(courseRepository.findById(courseId).get());
        courseEnrollment.setStudent(studentRepository.findById(studentId).get());
        courseEnrollmentRepository.save(courseEnrollment);
        return courseEnrollment;
    }

    public Page<CourseEnrollment> getAllEnrolledCoursesForStudent(Long studentId,Pageable pageable) {
        return courseEnrollmentRepository.findAllByStudentId(studentId,pageable);
    }

    public String unEnrollToCourse(Long enrollmentId) {
        courseEnrollmentRepository.deleteById(enrollmentId);
        return "Course UnEnrolled Successfully...!";
    }
}
