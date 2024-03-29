package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.PaypalPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaypalRepository extends JpaRepository<PaypalPayment, Long> {
}
