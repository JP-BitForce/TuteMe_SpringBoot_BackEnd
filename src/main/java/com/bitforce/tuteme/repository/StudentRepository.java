package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
