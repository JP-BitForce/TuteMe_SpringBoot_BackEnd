package com.bitforce.tuteme.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String city;
    private String district;
    private String gender;
    private String imageUrl;
    private String type;

    public User(String firstName, String lastName, String imageUrl, String city, String district, String gender,String type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
        this.city = city;
        this.district = district;
        this.gender = gender;
        this.type = type;
    }
}

