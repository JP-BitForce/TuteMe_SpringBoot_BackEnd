package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.dto.ControllerRequest.AddSystemFeedbackControllerRequest;
import com.bitforce.tuteme.dto.ServiceRequest.AddSystemFeedbackRequest;
import com.bitforce.tuteme.model.SystemFeedback;
import com.bitforce.tuteme.service.SystemFeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/feedback")
public class FeedbackController {
    private final Logger log = LoggerFactory.getLogger(FeedbackController.class);
    private final SystemFeedbackService systemFeedbackService;

    @Autowired
    public FeedbackController(SystemFeedbackService systemFeedbackService) {
        this.systemFeedbackService = systemFeedbackService;
    }

    @PostMapping("/add_system_Feedback")
    public SystemFeedback createSystemFeedback(@RequestBody AddSystemFeedbackControllerRequest request) {
        try {
            AddSystemFeedbackRequest addSystemFeedbackRequest = new AddSystemFeedbackRequest(
                    request.getUserId(),
                    request.getProfileId(),
                    request.getEmail(),
                    request.getFeedback(),
                    request.getRating(),
                    request.isServiceFind()
            );
            return systemFeedbackService.createFeedback(addSystemFeedbackRequest);
        } catch (Exception e) {
            log.error("Failed to add system feedback", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
