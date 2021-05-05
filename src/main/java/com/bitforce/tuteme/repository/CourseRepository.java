package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findAllByNameContaining(String value, Pageable pageable);

    Page<Course> findAllByCourseCategoryInAndTutorInAndCourseTypeInAndCoursePriceCategoryIn(
            List<CourseCategory> courseCategories,
            List<Tutor> tutors,
            List<CourseType> courseTypes,
            List<CoursePriceCategory> coursePriceCategories,
            Pageable pageable
    );

    Course findByTutor(Tutor tutor);

    Page<Course> findAllByCourseCategory(CourseCategory courseCategory, Pageable pageable);
}
