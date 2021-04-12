package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.configuration.JwtUtil;
import com.bitforce.tuteme.dto.ApiResponse;
import com.bitforce.tuteme.dto.ControllerResponse.AuthenticationControllerResponse;
import com.bitforce.tuteme.dto.LoginRequest;
import com.bitforce.tuteme.dto.ServiceResponse.AuthenticationResponse;
import com.bitforce.tuteme.dto.SignUpRequest;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            String response = authService.forgotPassword(email);
            ApiResponse apiResponse = new ApiResponse(
                    true,
                    response
            );
            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("forgotPassword/verifyCode")
    public ResponseEntity<?> verifyCode(@RequestParam String code, @RequestParam String email) {
        try {
            boolean confirm = authService.verifyCode(code, email);
            ApiResponse apiResponse;
            if (confirm) {
                apiResponse = new ApiResponse(true, "reset code confirmed");
            } else {
                apiResponse = new ApiResponse(false, "reset code not matched");
            }
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("forgotPassword/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam String password, @RequestParam String email) {
        try {
            String response = authService.resetPassword(password, email);
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
