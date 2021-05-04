package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.PaymentCard;
import com.bitforce.tuteme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long> {
    PaymentCard findByCardNo(Long cardNo);

    List<PaymentCard> findAllByUser(User user);
}
