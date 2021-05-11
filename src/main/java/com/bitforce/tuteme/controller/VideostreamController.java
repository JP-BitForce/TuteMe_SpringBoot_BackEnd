package com.bitforce.tuteme.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bitforce.tuteme.service.VideostreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class VideostreamController {
    @Autowired
    private VideostreamService handler;
    private final static File MP4_FILE = new File("D:/Videos/xc.mp4");

    @GetMapping("/videostream")
    public String home() {

        return "index";
    }

    @GetMapping("/byterange")
    public void byterange( HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute(VideostreamService.ATTR_FILE, MP4_FILE);
            handler.handleRequest(request, response);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error, please try again");
        }
    }
}
