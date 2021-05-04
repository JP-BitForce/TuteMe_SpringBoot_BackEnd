package com.bitforce.tuteme.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long cardNo;

    private String type;

    @OneToOne
    private User user;
}
