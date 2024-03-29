package com.bitforce.tuteme.controller;


import com.bitforce.tuteme.dto.StudentProfileDTO;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.service.StudentProfileService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/student/profile")
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    @PutMapping("/{userId}")
    public StudentProfileDTO updateStudentProfile(@RequestBody StudentProfileDTO studentProfileDTO, @PathVariable Long userId){
        return studentProfileService.updateStudentProfile(studentProfileDTO,userId);
    }

    @GetMapping("/{userId}")
    public StudentProfileDTO getStudentProfile(@PathVariable Long userId){
        return studentProfileService.getStudentProfile(userId);
    }

    @GetMapping("getAll/{page}")
    public ResponseEntity<Map<String, Object>> getTutorProfile(@PathVariable int page){
        return studentProfileService.getStudentProfiles(page);
    }

    @RequestMapping(value = "upload/{userId}", headers = "Content-Type= multipart/form-data", method = RequestMethod.POST)
    public User updateStudentProfilePicture(@RequestParam MultipartFile file, @PathVariable Long userId){
        return studentProfileService.updateStudentProfilePicture(file,userId);
    }

    @SneakyThrows
    @GetMapping(value = "uploads/profilePicture/student/{filename}",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImageResource(@PathVariable String filename) {
        return studentProfileService.getImageByte(filename);
    }


}
