package com.bitforce.tuteme.controller;


import com.bitforce.tuteme.dto.StudentProfileDTO;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.service.StudentProfileService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

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

    @PutMapping("upload/{userId}")
    public User updateStudentProfilePicture(@RequestParam MultipartFile file, @PathVariable Long userId){
        return studentProfileService.updateStudentProfilePicture(file,userId);
    }

    @SneakyThrows
    @GetMapping("uploads/profilePicture/student/{filename}")
    public ResponseEntity<Resource> getImageResource(@PathVariable String filename, HttpServletRequest request) {
        return studentProfileService.getImageResource(filename,request);
    }


}
