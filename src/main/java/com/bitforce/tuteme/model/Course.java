package com.bitforce.tuteme.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Lob
    private String description;
    private String imageUrl;
    private Double rating;
    private String duration;
    private BigDecimal price;

    @ManyToOne
    private Tutor tutor;

    @ManyToOne
    private CourseCategory courseCategory;

    @OneToOne
    private CoursePriceCategory coursePriceCategory;

    @OneToOne
    private CourseType courseType;

    @OneToMany
    private List<Schedule> schedules;
}
