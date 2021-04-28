package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.Course;
import com.bitforce.tuteme.model.CourseEnrollmentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseEnrollmentDetailRepository extends JpaRepository<CourseEnrollmentDetail, Long> {
    CourseEnrollmentDetail findByCourse(Course course);
}
