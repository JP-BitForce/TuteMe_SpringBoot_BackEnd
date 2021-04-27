package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseCategoryRepository extends JpaRepository<CourseCategory,Long> {
    List<CourseCategory> findAllByCategoryIn(List<String> category);
}
