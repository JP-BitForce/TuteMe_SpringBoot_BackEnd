package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.Course;
import com.bitforce.tuteme.model.Enrollment;
import com.bitforce.tuteme.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findAllByUser(User user);

    Page<Enrollment> findByUser(User user, Pageable pageable);

    Enrollment findByCourseAndUser(Course course, User user);
}
