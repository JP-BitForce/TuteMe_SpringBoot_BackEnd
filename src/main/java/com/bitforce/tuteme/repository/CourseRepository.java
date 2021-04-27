package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.Course;
import com.bitforce.tuteme.model.CourseCategory;
import com.bitforce.tuteme.model.Tutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {
    Page<Course> findAllByNameContaining(String value, Pageable pageable);

    Page<Course> findAllByCourseCategoryInAndTutorIn(
            List<CourseCategory> courseCategories,
            List<Tutor> tutors,
            Pageable pageable
    );
}
