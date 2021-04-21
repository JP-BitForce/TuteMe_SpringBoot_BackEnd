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
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private LocalDateTime start;

    private LocalDateTime end;

    private String backgroundColor;

    @Lob
    private String description;

    @OneToOne
    private User user;
}
