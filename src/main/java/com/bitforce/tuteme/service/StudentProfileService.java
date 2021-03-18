package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.StudentProfileDTO;
import com.bitforce.tuteme.model.Student;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.repository.StudentRepository;
import com.bitforce.tuteme.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class StudentProfileService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

@Transactional
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
}
