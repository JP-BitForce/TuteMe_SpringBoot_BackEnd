package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.configuration.CurrentUser;
import com.bitforce.tuteme.configuration.TutemeUserDetails;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/admin")
    public String getAdmin(@CurrentUser TutemeUserDetails currentUser) {
        return "Hello ";
    }
}
