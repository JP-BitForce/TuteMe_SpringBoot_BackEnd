package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.StudentProfileDTO;
import com.bitforce.tuteme.model.Student;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.model.UserAuth;
import com.bitforce.tuteme.repository.StudentRepository;
import com.bitforce.tuteme.repository.UserAuthRepository;
import com.bitforce.tuteme.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

@Service
@AllArgsConstructor
public class StudentProfileService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private final FileStorageService fileStorageService = new FileStorageService("profilePicture/student");

    public StudentProfileDTO updateStudentProfile(StudentProfileDTO studentProfileDTO, Long userId) {
        User user = userRepository.findById(userId).get();
        user.setFirstName(studentProfileDTO.getFirstName());
        user.setLastName(studentProfileDTO.getLastName());
        user.setCity(studentProfileDTO.getCity());
        user.setDistrict(studentProfileDTO.getDistrict());
        user.setGender(studentProfileDTO.getGender());
        userRepository.save(user);

        Student student = studentRepository.findByUserId(userId);
        student.setDob(studentProfileDTO.getDob());
        student.setLevel(studentProfileDTO.getLevel());
        student.setBio(studentProfileDTO.getBio());
        studentRepository.save(student);

        studentProfileDTO.setImageUrl(user.getImageUrl());
        return studentProfileDTO;
    }

    public StudentProfileDTO getStudentProfile(Long userId) {
        StudentProfileDTO studentProfileDTO = new StudentProfileDTO();
        User user = userRepository.findById(userId).get();
        UserAuth userAuth = userAuthRepository.findByUserId(userId);
        Student student = studentRepository.findByUserId(userId);

        studentProfileDTO.setFirstName(user.getFirstName());
        studentProfileDTO.setLastName(user.getLastName());
        studentProfileDTO.setEmail(userAuth.getEmail());
        studentProfileDTO.setDob(student.getDob());
        studentProfileDTO.setGender(user.getGender());
        studentProfileDTO.setImageUrl(user.getImageUrl());
        studentProfileDTO.setLevel(student.getLevel());
        studentProfileDTO.setCity(user.getCity());
        studentProfileDTO.setDistrict(user.getDistrict());
        studentProfileDTO.setBio(student.getBio());

        return studentProfileDTO;
    }

    public User updateStudentProfilePicture(MultipartFile file, Long userId) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/student/profile/uploads/profilePicture/student/")
                .path(fileName)
                .toUriString();

        User user = userRepository.findById(userId).get();
        user.setImageUrl(fileDownloadUri);
        userRepository.save(user);
        return user;
    }

    public ResponseEntity<Resource> getImageResource(String filename, HttpServletRequest request) {
        return fileStorageService.loadFileAsResource(filename,request);
    }
}
