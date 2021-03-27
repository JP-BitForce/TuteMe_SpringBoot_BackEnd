package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.model.SystemFeedback;
import com.bitforce.tuteme.service.SystemFeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/feedback")
public class SystemFeedbackController {
    private final SystemFeedbackService systemFeedbackService;

    @PostMapping
    public SystemFeedback createFeedback(@RequestBody SystemFeedback systemFeedback){
        return systemFeedbackService.createFeedback(systemFeedback);
    }

    @GetMapping
    public Page<SystemFeedback> getAllFeedbacks(Pageable pageable){
        return systemFeedbackService.getAllFeedbacks(pageable);
    }
}
