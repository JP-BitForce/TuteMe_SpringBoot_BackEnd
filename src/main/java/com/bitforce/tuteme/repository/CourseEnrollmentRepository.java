package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.CourseEnrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment,Long> {
    Page<CourseEnrollment> findAllByStudentId(Long studentId, Pageable pageable);

}
