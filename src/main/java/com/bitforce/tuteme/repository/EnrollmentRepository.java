package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.Course;
import com.bitforce.tuteme.model.Enrollment;
import com.bitforce.tuteme.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findAllByUser(User user);

    Page<Enrollment> findByUser(User user, Pageable pageable);

    Enrollment findByCourseAndUser(Course course, User user);

    @Query(value = "SELECT * FROM enrollment e inner join course c on c.id = e.course_id " +
            "inner join course_category cc on c.course_category_id = cc.id where cc.category in ?1 and e.user_id = ?2",
            nativeQuery = true)
    Page<Enrollment> filterEnrolledCourses(List<String> courseCategory, Long userId, Pageable pageable);

    @Query(value = "SELECT * FROM enrollment e inner join course c on c.id = e.course_id " +
            "where c.name like ?1% and e.user_id = ?2", nativeQuery = true)
    Page<Enrollment> searchEnrolledCourses(String value, Long userId, Pageable pageable);

}
