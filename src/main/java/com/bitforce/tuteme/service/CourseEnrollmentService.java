package com.bitforce.tuteme.service;

import com.bitforce.tuteme.model.CourseEnrollment;
import com.bitforce.tuteme.repository.CourseEnrollmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CourseEnrollmentService {

    private final CourseEnrollmentRepository courseEnrollmentRepository;

    public CourseEnrollment enrollToCourse(CourseEnrollment newCourseEnrollment) {
        CourseEnrollment courseEnrollment = new CourseEnrollment();
        courseEnrollment.setCourse(newCourseEnrollment.getCourse());
        courseEnrollment.setStudent(newCourseEnrollment.getStudent());
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
