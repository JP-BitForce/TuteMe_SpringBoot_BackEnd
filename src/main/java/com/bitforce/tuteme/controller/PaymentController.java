package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.configuration.JwtUtil;
import com.bitforce.tuteme.dto.ApiResponse;
import com.bitforce.tuteme.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class PaymentController {
    private final JwtUtil jwtUtil;
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(JwtUtil jwtUtil, PaymentService paymentService) {
        this.jwtUtil = jwtUtil;
        this.paymentService = paymentService;
    }

    @PostMapping("/payment")
    public ResponseEntity<?> Payment() {
        try {
            paymentService.GetPayment();
            return ResponseEntity.ok(200);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}