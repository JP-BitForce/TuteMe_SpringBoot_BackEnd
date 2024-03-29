package com.bitforce.tuteme.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    private String blogComment;

    private LocalDateTime date;

    @ManyToOne
    private User user;

    @OneToMany
    private List<CommentReply> commentReply;
}
