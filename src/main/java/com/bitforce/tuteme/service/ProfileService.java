package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.ProfileDTO;
import com.bitforce.tuteme.model.Student;
import com.bitforce.tuteme.model.Tutor;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.repository.StudentRepository;
import com.bitforce.tuteme.repository.TutorRepository;
import com.bitforce.tuteme.repository.UserAuthRepository;
import com.bitforce.tuteme.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProfileService {

    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;
    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;


    public ProfileDTO updateProfile(ProfileDTO profileDTO, Long userId){
        User user =  userRepository.findById(userId);
        user.setFirstName(profileDTO.getFirstName());
        user.setLastName(profileDTO.getLastName());
        user.setCity(profileDTO.getCity());
        user.setDistrict(profileDTO.getDistrict());
        user.setGender(profileDTO.getGender());
        user.setImageUrl(profileDTO.getImageUrl());
        userRepository.save(user);

        if(user.getType().equals("student")){
            Student student = studentRepository.findByUser(userId);
            student.setDob(profileDTO.getDob());
            student.setLevel(profileDTO.getLevel());
            student.setBio(profileDTO.getBio());
            studentRepository.save(student);
        }else if(user.getType().equals("tutor")){
            Tutor tutor = tutorRepository.findByUser(userId);
            tutor.setDescription(profileDTO.getDescription());
            tutor.setRating(profileDTO.getRating());
            tutorRepository.save(tutor);
        }
     return profileDTO;
    }
}
