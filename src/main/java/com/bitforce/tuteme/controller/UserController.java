package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.dto.ChangePasswordDTO;
import com.bitforce.tuteme.dto.UserProfilePictureDTO;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.repository.UserRepository;
import com.bitforce.tuteme.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/changePassword/{userId}")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO, @PathVariable Long userId) {
        return authService.changePassword(changePasswordDTO, userId);
    }

    @GetMapping("/profilePic/{userId}")
    public UserProfilePictureDTO getProfilePic(@PathVariable Long userId){
        User user =userRepository.findById(userId).get();
        UserProfilePictureDTO userProfilePictureDTO = new UserProfilePictureDTO();
        userProfilePictureDTO.setImageUrl(user.getImageUrl());
        return userProfilePictureDTO;
    }

}
