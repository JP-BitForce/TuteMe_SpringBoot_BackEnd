package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.CoursePriceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursePriceCategoryRepository extends JpaRepository<CoursePriceCategory, Long> {
}
