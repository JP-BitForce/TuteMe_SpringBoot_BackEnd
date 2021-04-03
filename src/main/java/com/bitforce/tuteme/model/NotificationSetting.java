package com.bitforce.tuteme.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class NotificationSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private boolean commentOneStep;
    private boolean commentBlog;
    private boolean courseUpdate;
    private boolean tutorUpdate;
    @OneToOne
    private User user;
}
