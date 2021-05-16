package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.StudentProfileDTO;;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.model.*;
import com.bitforce.tuteme.repository.EnrollmentRepository;
import com.bitforce.tuteme.repository.StudentRepository;
import com.bitforce.tuteme.repository.UserAuthRepository;
import com.bitforce.tuteme.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class StudentProfileService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final FileStorageService fileStorageService = new FileStorageService("profilePicture/student");

    public StudentProfileDTO updateStudentProfile(StudentProfileDTO studentProfileDTO, Long userId) {
        User user = userRepository.findById(userId).get();
        studentProfileDTO.setImageUrl(user.getImageUrl());
        BeanUtils.copyProperties(studentProfileDTO, user);
        userRepository.save(user);

        Student student = studentRepository.findByUserId(userId);
        BeanUtils.copyProperties(studentProfileDTO, student);
        studentRepository.save(student);

        return studentProfileDTO;
    }

    public StudentProfileDTO getStudentProfile(Long userId) {
        StudentProfileDTO studentProfileDTO = new StudentProfileDTO();
        User user = userRepository.findById(userId).get();
        UserAuth userAuth = userAuthRepository.findByUserId(userId);
        Student student = studentRepository.findByUserId(userId);

        int courseCount = getEnrolledCourses(user).size();
        int tutorsCount = getEnrolledCourses(user).size();

        BeanUtils.copyProperties(user, studentProfileDTO);
        BeanUtils.copyProperties(student, studentProfileDTO);
        studentProfileDTO.setId(student.getId());
        studentProfileDTO.setEmail(userAuth.getEmail());
        studentProfileDTO.setCourseCount(courseCount);
        studentProfileDTO.setTutorCount(tutorsCount);

        return studentProfileDTO;
    }

    public ResponseEntity<Map<String, Object>> getStudentProfiles(int page) {
        try {
            Pageable paging = PageRequest.of(page, 10);
            Page<Student> studentPage = studentRepository.findAll(paging);
            List<Student> students = studentPage.getContent();

            List<StudentProfileDTO> studentProfileDTOS = new ArrayList<>();
            for (Student student : students) {
                StudentProfileDTO studentProfileDTO = new StudentProfileDTO();
                BeanUtils.copyProperties(student, studentProfileDTO);
                User user = userRepository.findById(student.getUser().getId()).get();
                UserAuth userAuth = userAuthRepository.findByUserId(student.getUser().getId());
                BeanUtils.copyProperties(user, studentProfileDTO);
                studentProfileDTO.setId(student.getId());
                studentProfileDTO.setEmail(userAuth.getEmail());
                studentProfileDTO.setCourseCount(getEnrolledCourses(user).size());
                studentProfileDTO.setTutorCount(getEnrolledCourses(user).size());

                studentProfileDTOS.add(studentProfileDTO);

            }

            Map<String, Object> response = new HashMap<>();
            response.put("data", studentProfileDTOS);
            response.put("current", studentPage.getNumber());
            response.put("total", studentPage.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        return fileStorageService.loadFileAsResource(filename, request);
    }

    public byte[] getImageByte(String filename) throws IOException {
        return fileStorageService.convert(filename);
    }

    private List<Enrollment> getEnrolledCourses(User user) {
        return enrollmentRepository.findAllByUser(user);
    }

    public User getUser(Long studentId) throws EntityNotFoundException {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (!studentOptional.isPresent()) {
            throw new EntityNotFoundException("STUDENT_NOT_FOUND");
        }
        Student student = studentOptional.get();
        return student.getUser();
    }
}
