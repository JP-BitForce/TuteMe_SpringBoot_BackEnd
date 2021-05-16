package com.bitforce.tuteme.service;

import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.exception.MismatchException;
import com.bitforce.tuteme.model.Course;
import com.bitforce.tuteme.model.Resource;
import com.bitforce.tuteme.model.Tutor;
import com.bitforce.tuteme.repository.ResourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ResourceService {
    private final Logger log = LoggerFactory.getLogger(ResourceService.class);

    private final ResourceRepository resourceRepository;
    private final CourseService courseService;
    private final TutorProfileService tutorProfileService;
    private final FileStorageService fileStorageService = new FileStorageService("Resource/file");
    private final FileStorageService videoStorageService = new FileStorageService("Resource/video");

    public ResourceService(ResourceRepository resourceRepository,
                           CourseService courseService,
                           TutorProfileService tutorProfileService
    ) {
        this.resourceRepository = resourceRepository;
        this.courseService = courseService;
        this.tutorProfileService = tutorProfileService;
    }

    public String uploadFile(Long tutorId, Long courseId, String title, MultipartFile file) throws EntityNotFoundException, MismatchException {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/resource/uploads/Resource/file/")
                .path(fileName)
                .toUriString();

        Tutor tutor = tutorProfileService.getTutor(tutorId);
        Optional<Course> courseOptional = courseService.getCourse(courseId);
        if (!courseOptional.isPresent()) {
            throw new EntityNotFoundException("COURSE_NOT_FOUND");
        }
        Course course = courseOptional.get();
        if (course.getTutor() != tutor) {
            log.error("Given course and tutor course mismatch");
            throw new MismatchException("ENTITY_MISMATCH");
        }
        Resource resource = Resource
                .builder()
                .course(course)
                .title(title)
                .type("file")
                .uploaded(LocalDateTime.now())
                .url(fileDownloadUri)
                .build();
        resourceRepository.save(resource);
        return "file uploaded successfully";
    }

    public String uploadVideo(Long tutorId, Long courseId, String title, MultipartFile file) throws EntityNotFoundException, MismatchException {
        String fileName = videoStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/resource/uploads/Resource/video/")
                .path(fileName)
                .toUriString();

        Tutor tutor = tutorProfileService.getTutor(tutorId);
        Optional<Course> courseOptional = courseService.getCourse(courseId);
        if (!courseOptional.isPresent()) {
            throw new EntityNotFoundException("COURSE_NOT_FOUND");
        }
        Course course = courseOptional.get();
        if (course.getTutor() != tutor) {
            log.error("Given course and tutor course mismatch");
            throw new MismatchException("ENTITY_MISMATCH");
        }
        Resource resource = Resource
                .builder()
                .course(course)
                .title(title)
                .type("video")
                .uploaded(LocalDateTime.now())
                .url(fileDownloadUri)
                .build();
        resourceRepository.save(resource);
        return "file uploaded successfully";
    }

    public String uploadLink(Long tutorId, Long courseId, String link, String title) throws EntityNotFoundException, MismatchException {
        Tutor tutor = tutorProfileService.getTutor(tutorId);
        Optional<Course> courseOptional = courseService.getCourse(courseId);
        if (!courseOptional.isPresent()) {
            throw new EntityNotFoundException("COURSE_NOT_FOUND");
        }
        Course course = courseOptional.get();
        if (course.getTutor() != tutor) {
            log.error("Given course and tutor course mismatch");
            throw new MismatchException("ENTITY_MISMATCH");
        }
        Resource resource = Resource
                .builder()
                .course(course)
                .title(title)
                .type("link")
                .uploaded(LocalDateTime.now())
                .url(link)
                .build();
        resourceRepository.save(resource);
        return "file uploaded successfully";
    }
}
