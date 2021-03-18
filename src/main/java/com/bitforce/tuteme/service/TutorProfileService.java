package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.TutorProfileDTO;
import com.bitforce.tuteme.model.Tutor;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.model.UserAuth;
import com.bitforce.tuteme.repository.TutorRepository;
import com.bitforce.tuteme.repository.UserAuthRepository;
import com.bitforce.tuteme.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@AllArgsConstructor
public class TutorProfileService {

    private final TutorRepository tutorRepository;
    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private final FileStorageService fileStorageService = new FileStorageService("profilePicture/tutor");


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

    public TutorProfileDTO getTutorProfile(Long userId) {
        TutorProfileDTO tutorProfileDTO = new TutorProfileDTO();
        User user = userRepository.findById(userId).get();
        UserAuth userAuth = userAuthRepository.findByUserId(userId);
        Tutor tutor = tutorRepository.findByUserId(userId);

        tutorProfileDTO.setFirstName(user.getFirstName());
        tutorProfileDTO.setLastName(user.getLastName());
        tutorProfileDTO.setEmail(userAuth.getEmail());
        tutorProfileDTO.setGender(user.getGender());
        tutorProfileDTO.setImageUrl(user.getImageUrl());
        tutorProfileDTO.setCity(user.getCity());
        tutorProfileDTO.setDistrict(user.getDistrict());
        tutorProfileDTO.setDescription(tutor.getDescription());
        tutorProfileDTO.setRating(tutor.getRating());

        return tutorProfileDTO;
    }

    public User updateTutorProfilePicture(MultipartFile file, Long userId) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/tutor/profile/uploads/profilePicture/tutor/")
                .path(fileName)
                .toUriString();

        User user = userRepository.findById(userId).get();
        user.setImageUrl(fileDownloadUri);
        userRepository.save(user);
        return user;
    }
}
