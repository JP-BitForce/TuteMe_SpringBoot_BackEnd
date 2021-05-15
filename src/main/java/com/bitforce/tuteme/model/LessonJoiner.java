package com.bitforce.tuteme.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonJoiner {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String joinId;

    @OneToOne
    private Course course;

    @OneToOne
    private Tutor tutor;

    public LessonJoiner(String joinId, Course course, Tutor tutor) {
        this.joinId = joinId;
        this.course = course;
        this.tutor = tutor;
    }
}
