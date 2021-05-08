package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.UserSkills;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSkillsRepository extends JpaRepository<UserSkills,Long> {

    boolean existsByUserId(Long id);
    UserSkills findByUserId(Long id);
}
