package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.ServiceResponse.GetLandingPageContentsResponse;
import com.bitforce.tuteme.exception.AlreadyExistException;
import com.bitforce.tuteme.model.Subscription;
import com.bitforce.tuteme.model.SystemMessage;
import com.bitforce.tuteme.repository.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class LandingPageService {
    private final SystemMessageRepository systemMessageRepository;
    private final SystemFeedbackService systemFeedbackService;
    private final CourseRepository courseRepository;
    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;
    private final ScheduleRepository scheduleRepository;
    private final SubscriptionRepository subscriptionRepository;

    public LandingPageService(SystemMessageRepository systemMessageRepository,
                              SystemFeedbackService systemFeedbackService,
                              CourseRepository courseRepository,
                              TutorRepository tutorRepository,
                              StudentRepository studentRepository,
                              ScheduleRepository scheduleRepository,
                              SubscriptionRepository subscriptionRepository
    ) {
        this.systemMessageRepository = systemMessageRepository;
        this.systemFeedbackService = systemFeedbackService;
        this.courseRepository = courseRepository;
        this.tutorRepository = tutorRepository;
        this.studentRepository = studentRepository;
        this.scheduleRepository = scheduleRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public String sendSystemMessage(String name, String email, String message) {
        SystemMessage systemMessage = SystemMessage
                .builder()
                .email(email)
                .name(name)
                .message(message)
                .build();

        systemMessageRepository.save(systemMessage);
        return "message sent successfully, system will get back soon!";
    }

    public GetLandingPageContentsResponse getLandingPageContents() throws IOException {
        Map<String, Object> feedbacks = systemFeedbackService.getAllFeedbacks(0);

        int courses = courseRepository.numberOfCourses();
        int tutors = tutorRepository.numberOfCourses();
        int students = studentRepository.numberOfCourses();
        int schedules = scheduleRepository.numberOfCourses();

        Map<String, Object> counts = new HashMap<>();
        counts.put("courses", courses);
        counts.put("tutors", tutors);
        counts.put("students", students);
        counts.put("schedules", schedules);

        return new GetLandingPageContentsResponse(
                feedbacks,
                counts
        );
    }

    public String subscribe(String email) throws AlreadyExistException {
        Subscription existed = subscriptionRepository.findByEmail(email);
        if (existed == null) {
            Subscription subscription = Subscription
                    .builder()
                    .email(email)
                    .build();
            subscriptionRepository.save(subscription);
        } else {
            throw new AlreadyExistException("SUBSCRIPTION_ALREADY_EXIST");
        }
        return "successfully subscribed to system";
    }
}
