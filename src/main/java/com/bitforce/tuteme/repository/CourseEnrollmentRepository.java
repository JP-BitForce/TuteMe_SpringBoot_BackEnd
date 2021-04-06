package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.CourseEnrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment,Long> {
    Page<CourseEnrollment> findAllByStudentId(Long studentId, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM course WHERE id = (SELECT course_id FROM course_enrollment WHERE student_id=?1)", nativeQuery = true)
    Integer findAllCourseCountByStudentId(Long id);

    @Query(value = "SELECT COUNT(DISTINCT(tutor_id)) FROM course WHERE id = (SELECT course_id FROM course_enrollment WHERE student_id=?1)", nativeQuery = true)
    Integer findAllTutorsCountByStudentId(Long id);
}
