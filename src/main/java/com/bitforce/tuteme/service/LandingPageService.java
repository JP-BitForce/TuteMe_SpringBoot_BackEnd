package com.bitforce.tuteme.service;

import com.bitforce.tuteme.model.SystemMessage;
import com.bitforce.tuteme.repository.SystemMessageRepository;
import org.springframework.stereotype.Service;

@Service
public class LandingPageService {
    private final SystemMessageRepository systemMessageRepository;

    public LandingPageService(SystemMessageRepository systemMessageRepository) {
        this.systemMessageRepository = systemMessageRepository;
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
}
