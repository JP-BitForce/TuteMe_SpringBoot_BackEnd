package com.bitforce.tuteme.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaypalPayment{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String address;

    private String city;

    private String zip;

    private String mobile;

    private String cvv;

    private String exp;

    private String cardNo;

    private LocalDateTime date;

    @OneToOne
    private Payment payment;
}
