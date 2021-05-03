package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.PaymentPlan;
import com.bitforce.tuteme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentPlanRepository extends JpaRepository<PaymentPlan, Long> {
    PaymentPlan findByUser(User user);
}
