package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.configuration.AppProperties;
import com.bitforce.tuteme.configuration.JwtUtil;
import com.bitforce.tuteme.dto.ApiResponse;
import com.bitforce.tuteme.dto.ControllerResponse.AuthenticationControllerResponse;
import com.bitforce.tuteme.dto.LoginRequest;
import com.bitforce.tuteme.dto.ServiceResponse.AuthenticationResponse;
import com.bitforce.tuteme.dto.SignUpRequest;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.service.AuthService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final JwtUtil jwtUtil;
    private final AuthService authService;

    @Autowired
    public AuthController(JwtUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

 @Autowired
    AppProperties appProperties;

    @Transactional
    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUsers(@RequestBody LoginRequest request) {
        try {
            AuthenticationResponse authenticationResponse = authService.authenticateUser(
                    request,
                    jwtUtil
            );
            AuthenticationControllerResponse authResponse = AuthenticationControllerResponse
                    .builder()
                    .token(authenticationResponse.getToken())
                    .expirationInMilliseconds((int)appProperties.getAuth().getJwtExpirationInMs())
                    .email(authenticationResponse.getEmail())
                    .profileId(authenticationResponse.getProfileId())
                    .role(authenticationResponse.getRole())
                    .userId(authenticationResponse.getUserId())
                    .imgSrc(authService.getUserImg(authenticationResponse.getRole(), authenticationResponse.getUserId()))
                    .build();
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest, @RequestParam String userType) {
        try {
            log.info("Sign up info:: firstName: {}, lastName: {}, email: {}, type: {}",
                    signUpRequest.getFirstName(),
                    signUpRequest.getLastName(),
                    signUpRequest.getEmail(),
                    userType
            );
            return authService.registerUser(signUpRequest, userType);
        } catch (Exception e) {
            log.error("Unable to sign up");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
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
