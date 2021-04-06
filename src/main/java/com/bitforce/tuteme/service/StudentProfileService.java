package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.StudentProfileDTO;
import com.bitforce.tuteme.model.Student;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.model.UserAuth;
import com.bitforce.tuteme.repository.CourseEnrollmentRepository;
import com.bitforce.tuteme.repository.StudentRepository;
import com.bitforce.tuteme.repository.UserAuthRepository;
import com.bitforce.tuteme.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final CourseEnrollmentRepository courseEnrollmentRepository;
    private final FileStorageService fileStorageService = new FileStorageService("profilePicture/student");

    public StudentProfileDTO updateStudentProfile(StudentProfileDTO studentProfileDTO, Long userId) {
        User user = userRepository.findById(userId).get();
        studentProfileDTO.setImageUrl(user.getImageUrl());
        BeanUtils.copyProperties(studentProfileDTO,user);
        userRepository.save(user);

        Student student = studentRepository.findByUserId(userId);
        BeanUtils.copyProperties(studentProfileDTO,student);
        studentRepository.save(student);

        return studentProfileDTO;
    }

    public StudentProfileDTO getStudentProfile(Long userId) {
        StudentProfileDTO studentProfileDTO = new StudentProfileDTO();
        User user = userRepository.findById(userId).get();
        UserAuth userAuth = userAuthRepository.findByUserId(userId);
        Student student = studentRepository.findByUserId(userId);

        int courseCount = courseEnrollmentRepository.findAllCourseCountByStudentId(student.getId());
        int tutorsCount = courseEnrollmentRepository.findAllTutorsCountByStudentId(student.getId());

        BeanUtils.copyProperties(user,studentProfileDTO);
        BeanUtils.copyProperties(student,studentProfileDTO);
        studentProfileDTO.setEmail(userAuth.getEmail());
        studentProfileDTO.setCourseCount(courseCount);
        studentProfileDTO.setTutorCount(tutorsCount);

        return studentProfileDTO;
    }

    public Page<Student> getStudentProfiles(Pageable pageable) {
        return studentRepository.findAll(pageable);
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
