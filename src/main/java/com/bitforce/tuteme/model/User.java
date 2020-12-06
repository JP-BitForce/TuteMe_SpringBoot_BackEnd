package com.bitforce.tuteme.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
    @Entity
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int id;
        private String firstName;
        private String lastName;
        private String username;
        private String password;
        private String roles;
        private boolean active;

        public User() {
        }

    public User(String firstName,String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }
    }

