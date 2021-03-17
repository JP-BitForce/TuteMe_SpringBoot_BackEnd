package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAuthRepository extends JpaRepository<UserAuth,Long> {
    Optional<UserAuth> findByEmail(String username);

    boolean existsByEmail(String email);
}
