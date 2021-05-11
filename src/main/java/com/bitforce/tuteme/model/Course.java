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

    @OneToOne
    private CourseDuration courseDuration;

    public Course(String name,
                  String description,
                  String imageUrl,
                  Double rating,
                  String duration,
                  BigDecimal price,
                  Tutor tutor,
                  CourseCategory courseCategory,
                  CoursePriceCategory coursePriceCategory,
                  CourseType courseType,
                  List<Schedule> schedules,
                  CourseDuration courseDuration
    ) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.duration = duration;
        this.price = price;
        this.tutor = tutor;
        this.courseCategory = courseCategory;
        this.coursePriceCategory = coursePriceCategory;
        this.courseType = courseType;
        this.schedules = schedules;
        this.courseDuration = courseDuration;
    }
}
