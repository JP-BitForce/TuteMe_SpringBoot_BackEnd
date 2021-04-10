package com.bitforce.tuteme.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Lob
    private String content;

    private Integer likes;

    private LocalDateTime date;

    @Lob
    private String description;

    private String coverImgUrl;

    @OneToMany
    List<Comment> comments;

    @ManyToOne
    User user;

    public Blog(String title, String content, Integer likes, LocalDateTime date, String description, String coverImgUrl, User user) {
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.date = date;
        this.description = description;
        this.coverImgUrl = coverImgUrl;
        this.user = user;
    }
}
