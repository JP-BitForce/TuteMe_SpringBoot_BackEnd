package com.bitforce.tuteme.controller;


import com.bitforce.tuteme.dto.ProfileDTO;
import com.bitforce.tuteme.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    @PutMapping("/{userId}")
    public ProfileDTO updateProfile(@RequestBody ProfileDTO profileDTO, @PathVariable Long userId){
        return profileService.updateProfile(profileDTO,userId);
    }

}
