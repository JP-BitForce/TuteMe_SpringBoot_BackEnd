package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
    Optional<UserAuth> findByEmail(String username);

    boolean existsByEmail(String email);

    UserAuth findByUserId(Long userId);
}
