package com.bitforce.tuteme.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String content;
    private Integer likes;
    private LocalDateTime date;
    @OneToMany
    List<Comment> comments;
    @ManyToOne
    User user;
}
