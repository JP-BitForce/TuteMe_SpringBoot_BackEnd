package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.dto.ControllerRequest.FilterTutorByCategoryControllerRequest;
import com.bitforce.tuteme.dto.ServiceResponse.GetTutorsResponse;
import com.bitforce.tuteme.dto.TutorProfileDTO;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.service.TutorProfileService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/tutor/profile")
public class TutorProfileController {
    private static final Logger log = LoggerFactory.getLogger(TutorProfileController.class);
    private final TutorProfileService tutorProfileService;

    @PutMapping("/{userId}")
    public TutorProfileDTO updateTutorProfile(@RequestBody TutorProfileDTO tutorProfileDTO, @PathVariable Long userId) {
        try {
            return tutorProfileService.updateTutorProfile(tutorProfileDTO, userId);
        } catch (Exception e) {
            log.error("unable to update tutor profile");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public TutorProfileDTO getTutorProfile(@PathVariable Long userId) {
        try {
            return tutorProfileService.getTutorProfile(userId);
        } catch (Exception e) {
            log.error("unable to get tutor profile of userId:{}", userId);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/getAll/{page}")
    public ResponseEntity<?> getTutorProfiles(@PathVariable int page) {
        try {
            GetTutorsResponse response = tutorProfileService.getTutorProfiles(page);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to get tutors");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @PutMapping("upload/{userId}")
    public User updateTutorProfilePicture(@RequestParam MultipartFile file, @PathVariable Long userId) {
        try {
            return tutorProfileService.updateTutorProfilePicture(file, userId);
        } catch (Exception e) {
            log.error("unable to upload tutor profile picture, userId:{}", userId);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping(value = "/search_tutor_by_value")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> searchTutorByValue(@RequestParam String value, @RequestParam int page) {
        try {
            GetTutorsResponse response = tutorProfileService.searchTutorByValue(value, page);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to get tutor by value: {}", value);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/filter_tutor_by_category")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> filterTutorByCategory(@RequestBody FilterTutorByCategoryControllerRequest request) {
        try {
            GetTutorsResponse response = tutorProfileService.filterByCourseCategory(
                    request.getCategory(),
                    request.getPage()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to filter tutor by category: {}", request.getCategory());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping(value = "uploads/profilePicture/tutor/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImageResource(@PathVariable String filename) {
        return tutorProfileService.getImageByte(filename);
    }

}
