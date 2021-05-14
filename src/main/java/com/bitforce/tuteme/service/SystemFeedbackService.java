package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.ServiceRequest.AddSystemFeedbackRequest;
import com.bitforce.tuteme.dto.SystemFeedbackDTO;
import com.bitforce.tuteme.model.SystemFeedback;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.repository.SystemFeedbackRepository;
import com.bitforce.tuteme.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemFeedbackService {

    private final SystemFeedbackRepository systemFeedbackRepository;
    private final UserRepository userRepository;
    private final StudentProfileService studentProfileService;
    private final TutorProfileService tutorProfileService;

    public SystemFeedbackService(SystemFeedbackRepository systemFeedbackRepository,
                                 UserRepository userRepository,
                                 StudentProfileService studentProfileService,
                                 TutorProfileService tutorProfileService
    ) {
        this.systemFeedbackRepository = systemFeedbackRepository;
        this.userRepository = userRepository;
        this.studentProfileService = studentProfileService;
        this.tutorProfileService = tutorProfileService;
    }

    public SystemFeedback createFeedback(AddSystemFeedbackRequest request) {
        User user = userRepository.findById(Long.parseLong(request.getUserId())).get();
        SystemFeedback systemFeedback = new SystemFeedback(
                request.getFeedback(),
                request.getRating(),
                request.isServiceFind(),
                user
        );
        return systemFeedbackRepository.save(systemFeedback);
    }

    public Map<String, Object> getAllFeedbacks(int page) throws IOException {
        Pageable paging = PageRequest.of(page, 10);
        Page<SystemFeedback> feedbackPage = systemFeedbackRepository.findAll(paging);
        List<SystemFeedback> feedbacks = feedbackPage.getContent();

        List<SystemFeedbackDTO> systemFeedbackDTOS = new ArrayList<>();
        for (SystemFeedback systemFeedback : feedbacks) {
            SystemFeedbackDTO systemFeedbackDTO = new SystemFeedbackDTO();
            BeanUtils.copyProperties(systemFeedback, systemFeedbackDTO);
            systemFeedbackDTO.setUserId(systemFeedback.getUser().getId());
            systemFeedbackDTO.setUserName(systemFeedback.getUser().getFirstName() + " " + systemFeedback.getUser().getLastName());
            systemFeedbackDTO.setUserType(systemFeedback.getUser().getType());
            systemFeedbackDTO.setImageUrl(getImageByte(systemFeedback.getUser()));

            systemFeedbackDTOS.add(systemFeedbackDTO);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("data", systemFeedbackDTOS);
        response.put("current", feedbackPage.getNumber());
        response.put("total", feedbackPage.getTotalPages());

        return response;
    }

    private byte[] getImageByte(User user) throws IOException {
        String type = user.getType();
        String url = user.getImageUrl();
        if (url != null) {
            if (type.equals("student")) {
                String[] filename = url.trim().split("http://localhost:8080/api/student/profile/uploads/profilePicture/student/");
                return studentProfileService.getImageByte(filename[1]);
            } else {
                String[] filename = url.trim().split("http://localhost:8080/api/tutor/profile/uploads/profilePicture/tutor/");
                return tutorProfileService.getImageByte(filename[1]);
            }
        } else {
            return null;
        }
    }
}
