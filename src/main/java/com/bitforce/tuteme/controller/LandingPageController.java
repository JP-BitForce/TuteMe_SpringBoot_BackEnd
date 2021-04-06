package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.model.SystemFeedback;
import com.bitforce.tuteme.service.SystemFeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/landingPage")
public class LandingPageController {
    private final SystemFeedbackService systemFeedbackService;

    @GetMapping("/feedback")
    public Page<SystemFeedback> getAllFeedbacks(Pageable pageable){
        return systemFeedbackService.getAllFeedbacks(pageable);
    }
}
