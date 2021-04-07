package com.bitforce.tuteme.controller;


import com.bitforce.tuteme.dto.StudentProfileDTO;
import com.bitforce.tuteme.model.Student;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.service.StudentProfileService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
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

    @GetMapping()
    public Page<Student> getTutorProfile(Pageable pageable){
        return studentProfileService.getStudentProfiles(pageable);
    }

    @RequestMapping(value = "upload/{userId}", headers = "Content-Type= multipart/form-data", method = RequestMethod.POST)
    public User updateStudentProfilePicture(@RequestParam MultipartFile file, @PathVariable Long userId){
        return studentProfileService.updateStudentProfilePicture(file,userId);
    }

    @SneakyThrows
    @GetMapping(value = "uploads/profilePicture/student/{filename}",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImageResource(@PathVariable String filename, HttpServletRequest request) {
        return studentProfileService.getImageByte(filename);
    }


}
