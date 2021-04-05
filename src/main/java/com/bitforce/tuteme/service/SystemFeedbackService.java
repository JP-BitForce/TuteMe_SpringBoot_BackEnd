package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.ServiceRequest.AddSystemFeedbackRequest;
import com.bitforce.tuteme.model.SystemFeedback;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.repository.SystemFeedbackRepository;
import com.bitforce.tuteme.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Page<SystemFeedback> getAllFeedbacks(Pageable pageable) {
        return systemFeedbackRepository.findAll(pageable);
    }
}
