package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.dto.ApiResponse;
import com.bitforce.tuteme.dto.ControllerRequest.JoinIdControllerRequest;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.exception.MismatchException;
import com.bitforce.tuteme.service.LessonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/online_lesson")
public class OnlineLessonController {
    private final LessonService lessonService;

    public OnlineLessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping(value = "/create_join_id")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> createJoinId(@RequestBody JoinIdControllerRequest request) {
        try {
            String response = lessonService.createJoinId(request.getTutorId(), request.getCourseId(), request.getJoinId());
            ApiResponse apiResponse = new ApiResponse(true, response);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/get_course_joinId/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        try {
            String response = lessonService.getJoinIdByCourse(id);
            ApiResponse apiResponse = new ApiResponse(true, response);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/start_lesson")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> startLesson(@RequestBody JoinIdControllerRequest request) {
        try {
            String response = lessonService.verifyAndStart(request.getTutorId(), request.getCourseId(), request.getJoinId());
            ApiResponse apiResponse = new ApiResponse(true, response);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (EntityNotFoundException | MismatchException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
