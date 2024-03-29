package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.CourseDuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseDurationRepository extends JpaRepository<CourseDuration, Long> {
}
