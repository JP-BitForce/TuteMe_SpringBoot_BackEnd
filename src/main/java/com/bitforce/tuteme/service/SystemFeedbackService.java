package com.bitforce.tuteme.service;

import com.bitforce.tuteme.model.SystemFeedback;
import com.bitforce.tuteme.repository.SystemFeedbackRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SystemFeedbackService {

    private final SystemFeedbackRepository systemFeedbackRepository;

    public SystemFeedback createFeedback(SystemFeedback systemFeedback) {
        return systemFeedbackRepository.save(systemFeedback);
    }

    public Page<SystemFeedback> getAllFeedbacks(Pageable pageable) {
        return systemFeedbackRepository.findAll(pageable);
    }
}
