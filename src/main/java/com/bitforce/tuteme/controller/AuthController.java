package com.bitforce.tuteme.controller;


import com.bitforce.tuteme.configuration.jwt.JwtTokenProvider;
import com.bitforce.tuteme.dto.ApiResponse;
import com.bitforce.tuteme.configuration.jwt.JwtAuthenticationResponse;
import com.bitforce.tuteme.dto.LoginRequest;
import com.bitforce.tuteme.dto.SignUpRequest;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.model.UserAuth;
import com.bitforce.tuteme.repository.UserAuthRepository;
import com.bitforce.tuteme.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider tokenProvider;
    private UserAuthRepository userAuthRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, tokenProvider.expiryDate));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest, @RequestParam String userType) {
        if (userAuthRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        // Creating user's account
        UserAuth userAuth = new UserAuth(signUpRequest.getEmail(), signUpRequest.getPassword());
        userAuth.setPassword(passwordEncoder.encode(userAuth.getPassword()));

        String role;
        if (userType.equals("tutor")) {
            role = "TUTOR";
        } else if (userType.equals("admin")) {
            role = "ADMIN";
        } else {
            role = "STUDENT";
        }
        userAuth.setRole(("ROLE_" + role));

        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setType(role.toLowerCase());

        User newUser = userRepository.save(user);

        userAuth.setUser(newUser);

        UserAuth result = userAuthRepository.save(userAuth);



        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getEmail()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}
