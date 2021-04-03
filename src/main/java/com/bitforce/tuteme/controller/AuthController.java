package com.bitforce.tuteme.controller;


import com.bitforce.tuteme.dto.ChangePasswordDTO;
import com.bitforce.tuteme.dto.LoginRequest;
import com.bitforce.tuteme.dto.SignUpRequest;
import com.bitforce.tuteme.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

 private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest, @RequestParam String userType) {
        return authService.registerUser(signUpRequest,userType);

    }

    @PostMapping("/changePassword/{userId}")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO, @PathVariable Long userId){
        return authService.changePassword(changePasswordDTO,userId);
    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam String email){
        return authService.forgotPassword(email);
    }

    @PostMapping("forgotPassword/verifyCode")
    public boolean verifyCode(@RequestParam String code,@RequestParam String email){
        return authService.verifyCode(code,email);
    }

    @PostMapping("forgotPassword/resetPassword")
    public String resetPassword(@RequestParam String password,@RequestParam String email){
        return authService.resetPassword(password,email);
    }
}
