package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {
    Tutor findByUserId(Long userId);

}
