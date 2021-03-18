package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.TutorProfileDTO;
import com.bitforce.tuteme.model.Tutor;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.repository.TutorRepository;
import com.bitforce.tuteme.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TutorProfileService {

    private final TutorRepository tutorRepository;
    private final UserRepository userRepository;

    @Transactional
    public TutorProfileDTO updateTutorProfile(TutorProfileDTO tutorProfileDTO, Long userId) {

        User user = userRepository.findById(userId).get();
        user.setFirstName(tutorProfileDTO.getFirstName());
        user.setLastName(tutorProfileDTO.getLastName());
        user.setCity(tutorProfileDTO.getCity());
        user.setDistrict(tutorProfileDTO.getDistrict());
        user.setGender(tutorProfileDTO.getGender());
        userRepository.save(user);

        Tutor tutor = tutorRepository.findByUserId(userId);
        tutor.setDescription(tutorProfileDTO.getDescription());
        tutor.setRating(tutorProfileDTO.getRating());
        tutorRepository.save(tutor);

        tutorProfileDTO.setImageUrl(user.getImageUrl());
        return tutorProfileDTO;
    }
}
