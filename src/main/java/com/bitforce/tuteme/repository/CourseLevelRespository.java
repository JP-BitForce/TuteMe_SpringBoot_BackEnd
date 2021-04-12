package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.CourseLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseLevelRespository extends JpaRepository<CourseLevel, Long> {
}
