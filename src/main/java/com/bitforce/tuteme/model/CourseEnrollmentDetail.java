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
public class CourseEnrollmentDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime enrollmentDate;

    @OneToOne
    private Course course;

    @OneToOne
    private Payment payment;

}
