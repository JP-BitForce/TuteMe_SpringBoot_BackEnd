package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.LessonJoiner;
import com.bitforce.tuteme.model.Course;
import com.bitforce.tuteme.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonJoinerRepository extends JpaRepository<LessonJoiner, Long> {
    LessonJoiner findByCourse(Course course);

    LessonJoiner findByCourseAndTutor(Course course, Tutor tutor);
}
