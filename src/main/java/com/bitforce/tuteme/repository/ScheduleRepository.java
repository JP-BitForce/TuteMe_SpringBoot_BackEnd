package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("select count(id) from Schedule")
    int numberOfCourses();
}
