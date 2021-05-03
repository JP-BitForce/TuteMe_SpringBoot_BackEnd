package com.bitforce.tuteme.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bitforce.tuteme.service.VideostreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VideostreamController {
    @Autowired
    private VideostreamService handler;
    private final static File MP4_FILE = new File("D:/Videos/xc.mp4");

    // supports byte-range requests
    @GetMapping("/videostream")
    public String home() {

        return "index";
    }

    // supports byte-range requests
    @GetMapping("/byterange")
    public void byterange( HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute(VideostreamService.ATTR_FILE, MP4_FILE);
        handler.handleRequest(request, response);
    }
}
