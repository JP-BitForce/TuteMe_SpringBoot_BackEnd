package com.bitforce.tuteme.service;

import com.bitforce.tuteme.configuration.JwtUtil;
import com.bitforce.tuteme.dto.ApiResponse;
import com.bitforce.tuteme.dto.ChangePasswordDTO;
import com.bitforce.tuteme.dto.LoginRequest;
import com.bitforce.tuteme.dto.ServiceResponse.AuthenticationResponse;
import com.bitforce.tuteme.dto.SignUpRequest;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.model.Student;
import com.bitforce.tuteme.model.Tutor;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.model.UserAuth;
import com.bitforce.tuteme.repository.StudentRepository;
import com.bitforce.tuteme.repository.TutorRepository;
import com.bitforce.tuteme.repository.UserAuthRepository;
import com.bitforce.tuteme.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAuthRepository userAuthRepository;
    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;
    private final EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public AuthenticationResponse authenticateUser(LoginRequest loginRequest, JwtUtil jwtUtil) {
        UserAuth userAuth = userAuthRepository.findByEmail(loginRequest.getUsername()).get();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generate(authentication);
            userAuth.setLoginAttempt(0);
            userAuthRepository.save(userAuth);

            Long profileId = null;
            Long userId = userAuth.getUser().getId();
            if (userAuth.getRole().equals("ROLE_STUDENT")) {
                Student student = studentRepository.findByUserId(userId);
                profileId = student.getId();
            }
            if (userAuth.getRole().equals("ROLE_TUTOR")) {
                Tutor tutor = tutorRepository.findByUserId(userId);
                profileId = tutor.getId();
            }
            return AuthenticationResponse
                    .builder()
                    .token(jwt)
                    .userId(userId)
                    .email(userAuth.getEmail())
                    .profileId(profileId)
                    .role(userAuth.getRole())
                    .build();

        } catch (Exception e) {
            userAuth.setLoginAttempt(userAuth.getLoginAttempt() + 1);
            userAuthRepository.save(userAuth);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credentials are Incorrect");
        }
    }

    public ResponseEntity<?> registerUser(SignUpRequest signUpRequest, String userType) {
        if (userAuthRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email address is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        // Creating user's account
        UserAuth userAuth = new UserAuth(signUpRequest.getEmail(), signUpRequest.getPassword());
        userAuth.setPassword(passwordEncoder.encode(userAuth.getPassword()));
        userAuth.setRole(("ROLE_" + userType.toUpperCase()));

        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setType(userType.toLowerCase());
        User newUser = userRepository.save(user);

        if (userType.equals("student")) {
            Student student = new Student();
            student.setUser(newUser);
            studentRepository.save(student);
        } else if (userType.equals("tutor")) {
            Tutor tutor = new Tutor();
            tutor.setUser(newUser);
            tutorRepository.save(tutor);
        }
        userAuth.setUser(newUser);
        UserAuth result = userAuthRepository.save(userAuth);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getEmail()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    public ResponseEntity<?> changePassword(ChangePasswordDTO changePasswordDTO, Long userId) {
        UserAuth userAuth = userAuthRepository.findByUserId(userId);
        if (passwordEncoder.matches(changePasswordDTO.getOldPassword(), userAuth.getPassword())) {
            userAuth.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
            userAuthRepository.save(userAuth);
            return ResponseEntity.ok(new ApiResponse(true, "User Password changed successfully"));
        } else {
            return new ResponseEntity(new ApiResponse(false, "User password is Incorrect"), HttpStatus.BAD_REQUEST);
        }
    }

    public String forgotPassword(String email) throws EntityNotFoundException {
        if (userAuthRepository.findByEmail(email).isPresent()) {
            String passwordResetKey = RandomStringUtils.randomAlphanumeric(6);
            UserAuth userAuth = userAuthRepository.findByEmail(email).get();
            userAuth.setPasswordResetKey(passwordResetKey);
            userAuthRepository.save(userAuth);

            emailService.send(
                    email,
                    emailService.buildEmail(
                            userAuth.getUser().getFirstName(),
                            passwordResetKey
                    )
            );
            return "reset code sent successfully";
        } else {
            log.error("invalid email address provided, email: {}", email);
            throw new EntityNotFoundException("INVALID_EMAIL");
        }
    }

    public boolean verifyCode(String code, String email) {
        UserAuth userAuth = userAuthRepository.findByEmail(email).get();
        return userAuth.getPasswordResetKey().equals(code);
    }

    public String resetPassword(String password, String email) {
        UserAuth userAuth = userAuthRepository.findByEmail(email).get();
        userAuth.setPassword(passwordEncoder.encode(password));
        userAuth.setPasswordResetKey(null);
        userAuthRepository.save(userAuth);
        return "Password Reset successfully...!";
    }
}
