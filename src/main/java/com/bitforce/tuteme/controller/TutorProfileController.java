package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.dto.TutorProfileDTO;
import com.bitforce.tuteme.model.Tutor;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.service.TutorProfileService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @GetMapping()
    public Page<Tutor> getTutorProfiles(Pageable pageable){
        return tutorProfileService.getTutorProfiles(pageable);
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
