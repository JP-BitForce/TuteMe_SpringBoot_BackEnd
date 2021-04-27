package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.Enrollment;
import com.bitforce.tuteme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Enrollment findByUser(User user);
}
