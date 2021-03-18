package com.bitforce.tuteme.controller;


import com.bitforce.tuteme.dto.StudentProfileDTO;
import com.bitforce.tuteme.service.StudentProfileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

}
