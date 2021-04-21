package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.Event;
import com.bitforce.tuteme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByUser(User user);
}
