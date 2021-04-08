package com.bitforce.tuteme.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class SystemFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String feedback;
    private Double rating;
    private boolean finding;

    @OneToOne
    private User user;

    public SystemFeedback(String feedback, Double rating, boolean finding, User user) {
        this.feedback = feedback;
        this.rating = rating;
        this.finding = finding;
        this.user = user;
    }
}
