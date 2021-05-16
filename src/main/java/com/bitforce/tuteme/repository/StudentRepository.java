package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByUserId(Long userId);

    @Query("select count(id) from Student")
    int numberOfCourses();
}
