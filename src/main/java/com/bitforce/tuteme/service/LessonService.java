package com.bitforce.tuteme.service;

import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.exception.MismatchException;
import com.bitforce.tuteme.model.LessonJoiner;
import com.bitforce.tuteme.model.Course;
import com.bitforce.tuteme.model.Tutor;
import com.bitforce.tuteme.repository.LessonJoinerRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LessonService {
    private final LessonJoinerRepository lessonJoinerRepository;
    private final CourseService courseService;
    private final TutorProfileService tutorProfileService;

    public LessonService(LessonJoinerRepository lessonJoinerRepository,
                         CourseService courseService,
                         TutorProfileService tutorProfileService
    ) {
        this.lessonJoinerRepository = lessonJoinerRepository;
        this.courseService = courseService;
        this.tutorProfileService = tutorProfileService;
    }

    public String createJoinId(Long tutorId, Long courseId, String idPrefix) throws EntityNotFoundException {
        Optional<Course> courseOptional = courseService.getCourse(courseId);
        if (!courseOptional.isPresent()) {
            throw new EntityNotFoundException("COURSE_NOT_FOUND");
        }
        Course course = courseOptional.get();
        Tutor tutor = tutorProfileService.getTutor(tutorId);

        String postfix = RandomStringUtils.randomAlphanumeric(7);
        String joinId = idPrefix + postfix;

        LessonJoiner lessonJoiner = new LessonJoiner(joinId, course, tutor);
        lessonJoinerRepository.save(lessonJoiner);
        return joinId;
    }

    public String getJoinIdByCourse(Long courseId) throws EntityNotFoundException {
        Optional<Course> courseOptional = courseService.getCourse(courseId);
        if (!courseOptional.isPresent()) {
            throw new EntityNotFoundException("COURSE_NOT_FOUND");
        }
        Course course = courseOptional.get();
        LessonJoiner lessonJoiner = lessonJoinerRepository.findByCourse(course);

        if (lessonJoiner == null) {
            throw new EntityNotFoundException("JOIN_ID_NOT_FOUND");
        }

        return lessonJoiner.getJoinId();
    }

    public String verifyAndStart(Long tutorId, Long courseId, String joinId) throws EntityNotFoundException, MismatchException {
        Optional<Course> courseOptional = courseService.getCourse(courseId);
        if (!courseOptional.isPresent()) {
            throw new EntityNotFoundException("COURSE_NOT_FOUND");
        }
        Course course = courseOptional.get();
        Tutor tutor = tutorProfileService.getTutor(tutorId);

        LessonJoiner lessonJoiner = lessonJoinerRepository.findByCourseAndTutor(course, tutor);
        if (lessonJoiner == null) {
            throw new EntityNotFoundException("JOIN_ID_NOT_FOUND");
        }

        if (!lessonJoiner.getJoinId().equals(joinId)) {
            throw new MismatchException("JOIN_ID_MISMATCH");
        }

        return "joinId verified success";
    }
}
