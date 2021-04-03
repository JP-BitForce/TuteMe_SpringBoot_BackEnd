package com.bitforce.tuteme.model;

import com.sun.istack.NotNull;
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
    @NotNull
    private String email;
    private String password;
    private String role;
    private int loginAttempt;
    private String passwordResetKey;

    @OneToOne
    private User user;

    public UserAuth(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
