package com.bitforce.tuteme.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class UserAuth {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String email;
    private String password;
    private String role;

    @OneToOne
    private User user;

    public UserAuth(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
