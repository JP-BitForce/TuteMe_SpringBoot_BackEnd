package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.service.SystemFeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/landingPage")
public class LandingPageController {
    private final SystemFeedbackService systemFeedbackService;

    @GetMapping("/feedback/{page}")
    public ResponseEntity<Map<String, Object>> getAllFeedbacks(@PathVariable int page){
        return systemFeedbackService.getAllFeedbacks(page);
    }
}
