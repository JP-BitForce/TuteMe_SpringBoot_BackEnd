package com.bitforce.tuteme.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String description;
    private Double rating;
    private String facebook;
    private String twitter;
    private String instagram;
    private String linkedIn;
    private String fullName;

    @OneToOne
    private User user;
}
