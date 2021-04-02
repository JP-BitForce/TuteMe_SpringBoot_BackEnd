package com.bitforce.tuteme.service;

import com.bitforce.tuteme.model.SystemFeedback;
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

    public SystemFeedback createFeedback(Long userId, SystemFeedback systemFeedback) {
        SystemFeedback systemFeedback1 = new SystemFeedback();
        systemFeedback.setUser(userRepository.findById(userId).get());
        BeanUtils.copyProperties(systemFeedback,systemFeedback1);
        return systemFeedbackRepository.save(systemFeedback1);
    }

    public Page<SystemFeedback> getAllFeedbacks(Pageable pageable) {
        return systemFeedbackRepository.findAll(pageable);
    }
}
