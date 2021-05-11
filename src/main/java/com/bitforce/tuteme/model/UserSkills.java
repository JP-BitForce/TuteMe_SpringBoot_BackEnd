package com.bitforce.tuteme.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class UserSkills {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private User user;
    @ElementCollection
    private Set<String> SubjectSkills;
    @ElementCollection
    private Set<String> topSkills;
    @ElementCollection
    private Set<String> techSkills;

}
