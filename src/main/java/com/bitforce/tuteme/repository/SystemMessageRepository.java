package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.SystemMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemMessageRepository extends JpaRepository<SystemMessage, Long> {
}
