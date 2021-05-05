package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.Tutor;
import com.bitforce.tuteme.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {
    Tutor findByUserId(Long userId);

    List<Tutor> findAllByUserIn(List<User> users);

    Page<Tutor> findAllByFullNameContaining(String fullName, Pageable pageable);
}
