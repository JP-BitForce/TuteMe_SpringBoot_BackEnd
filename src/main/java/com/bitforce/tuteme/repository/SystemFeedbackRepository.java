package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.SystemFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemFeedbackRepository extends JpaRepository<SystemFeedback,Long> {
}
