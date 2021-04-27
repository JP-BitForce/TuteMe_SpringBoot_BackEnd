package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseTypeRepository extends JpaRepository<CourseType, Long> {
    List<CourseType> findAllByTitleIn(List<String> type);
}
