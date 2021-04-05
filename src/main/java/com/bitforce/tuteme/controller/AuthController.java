package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.configuration.JwtUtil;
import com.bitforce.tuteme.dto.ControllerResponse.AuthenticationControllerResponse;
import com.bitforce.tuteme.dto.LoginRequest;
import com.bitforce.tuteme.dto.ServiceResponse.AuthenticationResponse;
import com.bitforce.tuteme.dto.SignUpRequest;
import com.bitforce.tuteme.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final AuthService authService;

    @Autowired
    public AuthController(JwtUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @Value("${app.jwtExpirationInMs}")
    private int expiresIn;

    @Transactional
    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUsers(@RequestBody LoginRequest request) {
        AuthenticationResponse authenticationResponse = authService.authenticateUser(
          request,
          jwtUtil
        );
        AuthenticationControllerResponse authResponse = AuthenticationControllerResponse
                .builder()
                .token(authenticationResponse.getToken())
                .expirationInMilliseconds(expiresIn)
                .email(authenticationResponse.getEmail())
                .profileId(authenticationResponse.getProfileId())
                .role(authenticationResponse.getRole())
                .userId(authenticationResponse.getUserId())
                .build();
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest, @RequestParam String userType) {
        return authService.registerUser(signUpRequest, userType);

    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam String email) {
        return authService.forgotPassword(email);
    }

    @PostMapping("forgotPassword/verifyCode")
    public boolean verifyCode(@RequestParam String code, @RequestParam String email) {
        return authService.verifyCode(code, email);
    }

    @PostMapping("forgotPassword/resetPassword")
    public String resetPassword(@RequestParam String password, @RequestParam String email) {
        return authService.resetPassword(password, email);
    }
}
