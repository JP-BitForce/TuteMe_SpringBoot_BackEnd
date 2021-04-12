package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.ServiceRequest.AddSystemFeedbackRequest;
import com.bitforce.tuteme.model.SystemFeedback;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.repository.SystemFeedbackRepository;
import com.bitforce.tuteme.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SystemFeedbackService {

    private final SystemFeedbackRepository systemFeedbackRepository;
    private final UserRepository userRepository;

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

    public ResponseEntity<Map<String, Object>> getAllFeedbacks(int page) {
        try {
            Pageable paging = PageRequest.of(page, 10);
            Page<SystemFeedback> feedbackPage = systemFeedbackRepository.findAll(paging);
            List<SystemFeedback> feedbacks = feedbackPage.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("data", feedbacks);
            response.put("current", feedbackPage.getNumber());
            response.put("total", feedbackPage.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
