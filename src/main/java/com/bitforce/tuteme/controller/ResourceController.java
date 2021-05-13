package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.dto.ApiResponse;
import com.bitforce.tuteme.dto.ControllerRequest.UploadFileControllerRequest;
import com.bitforce.tuteme.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/resource")
public class ResourceController {
    private static final Logger log = LoggerFactory.getLogger(ResourceController.class);

    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @RequestMapping(value = "/upload_file", headers = "Content-Type= multipart/form-data", method = RequestMethod.POST)
    public ResponseEntity<?> uploadFileResource(
            @RequestPart("upload") UploadFileControllerRequest request, @RequestPart("file") MultipartFile file) {
        try {
            String response = resourceService.uploadFile(
                    request.getTutorId(),
                    request.getCourseId(),
                    request.getTitle(),
                    file
            );
            ApiResponse apiResponse = new ApiResponse(true, response);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("unable to upload file: file");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(value = "/upload_video", headers = "Content-Type= multipart/form-data", method = RequestMethod.POST)
    public ResponseEntity<?> uploadVideoResource(
            @RequestPart("upload") UploadFileControllerRequest request, @RequestPart("file") MultipartFile file) {
        try {
            String response = resourceService.uploadVideo(
                    request.getTutorId(),
                    request.getCourseId(),
                    request.getTitle(),
                    file
            );
            ApiResponse apiResponse = new ApiResponse(true, response);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("unable to upload file: video");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(value = "/upload_link")
    public ResponseEntity<?> uploadLinkResource(@RequestBody UploadFileControllerRequest request) {
        try {
            String response = resourceService.uploadLink(
                    request.getTutorId(),
                    request.getCourseId(),
                    request.getLink(),
                    request.getTitle()
            );
            ApiResponse apiResponse = new ApiResponse(true, response);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("unable to upload file: link");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
