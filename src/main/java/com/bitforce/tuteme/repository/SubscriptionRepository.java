package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findByEmail(String email);
}
