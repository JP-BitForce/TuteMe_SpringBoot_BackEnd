package com.bitforce.tuteme.service;

import com.bitforce.tuteme.configuration.jwt.JwtAuthenticationResponse;
import com.bitforce.tuteme.configuration.jwt.JwtTokenProvider;
import com.bitforce.tuteme.dto.ApiResponse;
import com.bitforce.tuteme.dto.ChangePasswordDTO;
import com.bitforce.tuteme.dto.LoginRequest;
import com.bitforce.tuteme.dto.SignUpRequest;
import com.bitforce.tuteme.model.Student;
import com.bitforce.tuteme.model.Tutor;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.model.UserAuth;
import com.bitforce.tuteme.repository.StudentRepository;
import com.bitforce.tuteme.repository.TutorRepository;
import com.bitforce.tuteme.repository.UserAuthRepository;
import com.bitforce.tuteme.repository.UserRepository;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final UserAuthRepository userAuthRepository;
    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;
    private final EmailService emailService;

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        String jwt = null;
        Authentication authentication = null;
        UserAuth userAuth = userAuthRepository.findByEmail(loginRequest.getUsername()).get();
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            jwt = tokenProvider.generateToken(authentication);
            userAuth.setLoginAttempt(0);
            userAuthRepository.save(userAuth);
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, tokenProvider.expiryDate));
        } catch (Exception e) {
            userAuth.setLoginAttempt(userAuth.getLoginAttempt() + 1);
            userAuthRepository.save(userAuth);
            return new ResponseEntity(new ApiResponse(false, "Credentials are Incorrect"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> registerUser(SignUpRequest signUpRequest, String userType) {
        if (userAuthRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
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
        UserAuth userAuth =userAuthRepository.findByUserId(userId);
        if (passwordEncoder.matches(changePasswordDTO.getOldPassword(),userAuth.getPassword())){
            userAuth.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
            userAuthRepository.save(userAuth);
            return ResponseEntity.ok(new ApiResponse(true, "User Password changed successfully"));
        }else {
            return new  ResponseEntity(new ApiResponse(false, "User password is Incorrect"),HttpStatus.BAD_REQUEST);
        }
    }

    public String forgotPassword(String email) {
        String passwordResetKey = RandomString.make(6);
        UserAuth userAuth = userAuthRepository.findByEmail(email).get();
        userAuth.setPasswordResetKey(passwordResetKey);
        userAuthRepository.save(userAuth);

        emailService.send(email,emailService.buildEmail(userAuth.getUser().getFirstName(),passwordResetKey));
        return passwordResetKey;
    }

    public boolean verifyCode(String code,String email) {
        UserAuth userAuth = userAuthRepository.findByEmail(email).get();
        if (userAuth.getPasswordResetKey().equals(code))
            return true;
        else
        return false;
    }

    public String resetPassword(String password, String email) {
        UserAuth userAuth = userAuthRepository.findByEmail(email).get();
        userAuth.setPassword(passwordEncoder.encode(password));
        userAuthRepository.save(userAuth);
        return "Password Reset successfully...!";
    }
}
