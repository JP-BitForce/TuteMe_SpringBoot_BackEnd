package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.dto.ServiceResponse.GetTutorsResponse;
import com.bitforce.tuteme.dto.TutorProfileDTO;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.service.TutorProfileService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/tutor/profile")
public class TutorProfileController {

    private final TutorProfileService tutorProfileService;

    @PutMapping("/{userId}")
    public TutorProfileDTO updateTutorProfile(@RequestBody TutorProfileDTO tutorProfileDTO , @PathVariable Long userId){
        return tutorProfileService.updateTutorProfile(tutorProfileDTO,userId);
    }

    @GetMapping("/{userId}")
    public TutorProfileDTO getTutorProfile(@PathVariable Long userId){
        return tutorProfileService.getTutorProfile(userId);
    }
    @GetMapping("/getAll/{page}")
    public GetTutorsResponse getTutorProfiles(@PathVariable int page){
        try {
            return tutorProfileService.getTutorProfiles(page);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @PutMapping("upload/{userId}")
    public User updateTutorProfilePicture(@RequestParam MultipartFile file, @PathVariable Long userId){
        return tutorProfileService.updateTutorProfilePicture(file,userId);
    }

    @SneakyThrows
    @GetMapping(value = "uploads/profilePicture/tutor/{filename}",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImageResource(@PathVariable String filename) {
        return tutorProfileService.getImageByte(filename);
    }

    }
