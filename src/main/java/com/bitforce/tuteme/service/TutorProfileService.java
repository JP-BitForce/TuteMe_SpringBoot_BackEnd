package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.TutorProfileDTO;
import com.bitforce.tuteme.model.Tutor;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.model.UserAuth;
import com.bitforce.tuteme.repository.TutorRepository;
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
public class TutorProfileService {

    private final TutorRepository tutorRepository;
    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private final FileStorageService fileStorageService = new FileStorageService("profilePicture/tutor");

    public TutorProfileDTO updateTutorProfile(TutorProfileDTO tutorProfileDTO, Long userId) {

        User user = userRepository.findById(userId).get();
        tutorProfileDTO.setImageUrl(user.getImageUrl());
        BeanUtils.copyProperties(tutorProfileDTO,user);
        userRepository.save(user);

        Tutor tutor = tutorRepository.findByUserId(userId);
        BeanUtils.copyProperties(tutorProfileDTO,tutor);
        tutorRepository.save(tutor);

        return tutorProfileDTO;
    }

    public TutorProfileDTO getTutorProfile(Long userId) {
        TutorProfileDTO tutorProfileDTO = new TutorProfileDTO();
        User user = userRepository.findById(userId).get();
        UserAuth userAuth = userAuthRepository.findByUserId(userId);
        Tutor tutor = tutorRepository.findByUserId(userId);

        BeanUtils.copyProperties(user,tutorProfileDTO);
        BeanUtils.copyProperties(tutor,tutorProfileDTO);
        tutorProfileDTO.setEmail(userAuth.getEmail());

        return tutorProfileDTO;
    }

    public Page<Tutor> getTutorProfiles(Pageable pageable) {
        return tutorRepository.findAll(pageable);
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

    public ResponseEntity<Resource> getImageResource(String filename, HttpServletRequest request) {
        return fileStorageService.loadFileAsResource(filename,request);
    }
}
