package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
    Tutor findByUser(Long userId);
}
