package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.ServiceResponse.GetTutorsResponse;
import com.bitforce.tuteme.dto.TutorProfileDTO;
import com.bitforce.tuteme.model.Tutor;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.model.UserAuth;
import com.bitforce.tuteme.repository.TutorRepository;
import com.bitforce.tuteme.repository.UserAuthRepository;
import com.bitforce.tuteme.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        BeanUtils.copyProperties(tutorProfileDTO, user);
        userRepository.save(user);

        Tutor tutor = tutorRepository.findByUserId(userId);
        BeanUtils.copyProperties(tutorProfileDTO, tutor);
        tutorRepository.save(tutor);

        return tutorProfileDTO;
    }

    public TutorProfileDTO getTutorProfile(Long userId) {
        TutorProfileDTO tutorProfileDTO = new TutorProfileDTO();
        User user = userRepository.findById(userId).get();
        UserAuth userAuth = userAuthRepository.findByUserId(userId);
        Tutor tutor = tutorRepository.findByUserId(userId);

        BeanUtils.copyProperties(user, tutorProfileDTO);
        BeanUtils.copyProperties(tutor, tutorProfileDTO);
        tutorProfileDTO.setEmail(userAuth.getEmail());

        return tutorProfileDTO;
    }

    public GetTutorsResponse getTutorProfiles(int page) {
        Pageable paging = PageRequest.of(page, 10);
        Page<Tutor> tutorPage = tutorRepository.findAll(paging);
        List<Tutor> tutors = tutorPage.getContent();

        List<TutorProfileDTO> tutorProfileDTOS = new ArrayList<>();
        for (Tutor tutor : tutors) {
            TutorProfileDTO tutorProfileDTO = new TutorProfileDTO();
            User user = userRepository.findById(tutor.getUser().getId()).get();
            UserAuth userAuth = userAuthRepository.findByUserId(tutor.getUser().getId());
            BeanUtils.copyProperties(tutor, tutorProfileDTO);
            BeanUtils.copyProperties(user, tutorProfileDTO);
            tutorProfileDTO.setId(tutor.getId());
            tutorProfileDTO.setEmail(userAuth.getEmail());

            tutorProfileDTOS.add(tutorProfileDTO);
        }
        return new GetTutorsResponse(
                tutors.stream().map(tutor -> new GetTutorsResponse.Tutor(
                                tutor.getId(),
                                tutor.getUser().getFirstName(),
                                tutor.getUser().getFirstName(),
                                getUserEmail(tutor.getUser()),
                                tutor.getUser().getGender(),
                                getImageSource(tutor.getUser().getImageUrl()),
                                tutor.getUser().getCity(),
                                tutor.getUser().getDistrict(),
                                tutor.getDescription(),
                                tutor.getRating()
                        )
                ).collect(Collectors.toList()),
                tutorPage.getTotalPages(),
                tutorPage.getNumber()
        );
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

    public List<Tutor> getTutorsByName(List<String> userNameList) {
        List<String> firstNameList = new ArrayList<>();
        List<String> lastNameList = new ArrayList<>();
        for (String userName : userNameList) {
            String[] name = userName.split(" ");
            firstNameList.add(name[0]);
            lastNameList.add(name[1]);
        }
        List<User> users = userRepository.findAllByFirstNameInAndLastNameIn(firstNameList, lastNameList);
        return tutorRepository.findAllByUserIn(users);
    }

    public ResponseEntity<Resource> getImageResource(String filename, HttpServletRequest request) {
        return fileStorageService.loadFileAsResource(filename, request);
    }

    public byte[] getImageByte(String filename) throws IOException {
        return fileStorageService.convert(filename);
    }

    public String getUserEmail(User user) {
        return userAuthRepository.findByUserId(user.getId()).getEmail();
    }

    @SneakyThrows
    public byte[] getImageSource(String url) {
        if (url != null) {
            String[] filename = url.trim().split("http://localhost:8080/api/tutor/profile/uploads/profilePicture/tutor/");
            return getImageByte(filename[1]);
        }
        return null;
    }
}
