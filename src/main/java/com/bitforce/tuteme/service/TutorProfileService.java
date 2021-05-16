package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.ServiceRequest.UpdateTutorProfileRequest;
import com.bitforce.tuteme.dto.ServiceResponse.GetTutorsResponse;
import com.bitforce.tuteme.dto.ServiceResponse.TutorResponse;
import com.bitforce.tuteme.dto.TutorProfileDTO;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.model.*;
import com.bitforce.tuteme.repository.*;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TutorProfileService {

    private final TutorRepository tutorRepository;
    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private final CourseRepository courseRepository;
    private final CourseCategoryRepository courseCategoryRepository;
    private final FileStorageService fileStorageService = new FileStorageService("profilePicture/tutor");

    public TutorResponse updateTutorProfile(UpdateTutorProfileRequest request) throws EntityNotFoundException {
        User user = getUser(request.getUserId());

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCity(request.getCity());
        user.setGender(request.getGender());
        user.setDistrict(request.getDistrict());
        userRepository.save(user);

        UserAuth userAuth = userAuthRepository.findByUserId(user.getId());
        userAuth.setEmail(request.getEmail());
        userAuthRepository.save(userAuth);

        Tutor tutor = tutorRepository.findByUserId(user.getId());

        tutor.setDescription(request.getBio());
        tutor.setFullName(request.getFirstName() + " " + request.getLastName());
        tutorRepository.save(tutor);

        return new TutorResponse(
                tutor.getId(),
                user.getFirstName(),
                user.getLastName(),
                userAuth.getEmail(),
                user.getGender(),
                user.getImageUrl(),
                user.getCity(),
                user.getDistrict(),
                tutor.getDescription(),
                tutor.getFacebook(),
                tutor.getTwitter(),
                tutor.getInstagram(),
                tutor.getLinkedIn()
        );
    }

    public TutorResponse getTutorProfile(Long tutorId) throws EntityNotFoundException {
        Tutor tutor = getTutor(tutorId);
        User user = tutor.getUser();
        UserAuth userAuth = userAuthRepository.findByUserId(user.getId());

        return new TutorResponse(
                tutor.getId(),
                user.getFirstName(),
                user.getLastName(),
                userAuth.getEmail(),
                user.getGender(),
                user.getImageUrl(),
                user.getCity(),
                user.getDistrict(),
                tutor.getDescription(),
                tutor.getFacebook(),
                tutor.getTwitter(),
                tutor.getInstagram(),
                tutor.getLinkedIn()
        );
    }

    public GetTutorsResponse getTutorProfiles(int page) {
        Pageable paging = PageRequest.of(page, 10);
        Page<Tutor> tutorPage = tutorRepository.findAll(paging);
        return getTutorsResponse(tutorPage);
    }

    public User updateTutorProfilePicture(MultipartFile file, Long userId) throws EntityNotFoundException {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/tutor/profile/uploads/profilePicture/tutor/")
                .path(fileName)
                .toUriString();

        User user = getUser(userId);
        user.setImageUrl(fileDownloadUri);
        userRepository.save(user);
        return user;
    }

    public GetTutorsResponse searchTutorByValue(String value, int page) {
        Pageable paging = PageRequest.of(page, 10);
        Page<Tutor> tutorPage = tutorRepository.findAllByFullNameContaining(value, paging);
        return getTutorsResponse(tutorPage);
    }

    public GetTutorsResponse filterByCourseCategory(String category, int page) {
        Pageable paging = PageRequest.of(page, 10);
        CourseCategory courseCategory = courseCategoryRepository.findByCategory(category);
        Page<Course> courses = courseRepository.findAllByCourseCategory(courseCategory, paging);
        return new GetTutorsResponse(
                courses.getContent().stream().map(course -> new GetTutorsResponse.Tutor(
                        course.getTutor().getId(),
                        course.getTutor().getUser().getFirstName(),
                        course.getTutor().getUser().getLastName(),
                        getUserEmail(course.getTutor().getUser()),
                        course.getTutor().getUser().getGender(),
                        getImageSource(course.getTutor().getUser().getImageUrl()),
                        course.getTutor().getUser().getCity(),
                        course.getTutor().getUser().getDistrict(),
                        course.getTutor().getDescription(),
                        course.getTutor().getRating(),
                        getCourseName(course.getTutor()),
                        getCourseId(course.getTutor()),
                        course.getTutor().getFacebook(),
                        course.getTutor().getTwitter(),
                        course.getTutor().getInstagram(),
                        course.getTutor().getLinkedIn()
                )).collect(Collectors.toList()),
                courses.getTotalPages(),
                courses.getNumber()
        );
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

    public byte[] getImageByte(String filename) throws IOException {
        return fileStorageService.convert(filename);
    }

    public String getUserEmail(User user) {
        return userAuthRepository.findByUserId(user.getId()).getEmail();
    }

    public Tutor getTutor(Long id) throws EntityNotFoundException {
        Optional<Tutor> tutorOptional = tutorRepository.findById(id);
        if (!tutorOptional.isPresent()) {
            throw new EntityNotFoundException("TUTOR_NOT_FOUND");
        }
        return tutorOptional.get();
    }

    @SneakyThrows
    public byte[] getImageSource(String url) {
        if (url != null) {
            String[] filename = url.trim().split("http://localhost:8080/api/tutor/profile/uploads/profilePicture/tutor/");
            return getImageByte(filename[1]);
        } else {
            return getImageByte("16160622633711.png");
        }
    }

    private Course getCourseByTutor(Tutor tutor) {
        return courseRepository.findByTutor(tutor);
    }

    private String getCourseName(Tutor tutor) {
        Course course = getCourseByTutor(tutor);
        if (course != null) {
            return course.getName();
        }
        return null;
    }

    private Long getCourseId(Tutor tutor) {
        Course course = getCourseByTutor(tutor);
        if (course != null) {
            return course.getId();
        }
        return null;
    }

    private User getUser(Long userId) throws EntityNotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new EntityNotFoundException("USER_NOT_FOUND");
        }
        return userOptional.get();
    }

    private GetTutorsResponse getTutorsResponse(Page<Tutor> tutorPage) {
        return new GetTutorsResponse(
                tutorPage.getContent().stream().map(tutor -> new GetTutorsResponse.Tutor(
                                tutor.getId(),
                                tutor.getUser().getFirstName(),
                                tutor.getUser().getLastName(),
                                getUserEmail(tutor.getUser()),
                                tutor.getUser().getGender(),
                                getImageSource(tutor.getUser().getImageUrl()),
                                tutor.getUser().getCity(),
                                tutor.getUser().getDistrict(),
                                tutor.getDescription(),
                                tutor.getRating(),
                                getCourseName(tutor),
                                getCourseId(tutor),
                                tutor.getFacebook(),
                                tutor.getTwitter(),
                                tutor.getInstagram(),
                                tutor.getLinkedIn()
                        )
                ).collect(Collectors.toList()),
                tutorPage.getTotalPages(),
                tutorPage.getNumber()
        );
    }
}
