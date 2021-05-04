package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.dto.ApiResponse;
import com.bitforce.tuteme.dto.ServiceResponse.GetPaymentSummaryFactsResponse;
import com.bitforce.tuteme.dto.ServiceResponse.GetPaymentsResponse;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/payments")
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping(value = "/get_payments")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getPayments(@RequestParam Long uId, @RequestParam int page) {
        try {
            GetPaymentsResponse getPaymentsResponse = paymentService.getPayments(uId, page);
            return new ResponseEntity<>(getPaymentsResponse, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            log.error("Unable to get payments for userId: {}", uId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to get payments for userId: {}", uId);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/upgrade_plan")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> filterCourses(@RequestParam long uId, @RequestParam String plan) {
        try {
            String response = paymentService.upgradePlan(uId, plan);
            ApiResponse apiResponse = new ApiResponse(true, response);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            log.error("Unable to upgrade plan for userId: {}, due to bad request", uId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request, please try valid value");
        } catch (Exception e) {
            log.error("Unable to upgrade plan for userId: {}", uId);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error, please try again");
        }
    }

    @GetMapping(value = "/get_payment_plan/{userId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getPaymentPlan(@PathVariable Long userId) {
        try {
            String response = paymentService.getPaymentPlanByUser(userId);
            ApiResponse apiResponse = new ApiResponse(true, response);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            log.error("Unable to upgrade plan for userId: {}, due to bad request", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request, please try valid value");
        } catch (Exception e) {
            log.error("Unable to upgrade plan for userId: {}", userId);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error, please try again");
        }
    }

    @GetMapping(value = "/get_payment_summary_facts/{userId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getPaymentSummaryFacts(@PathVariable Long userId) {
        try {
            GetPaymentSummaryFactsResponse response = paymentService.getPaymentSummaryFacts(userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            log.error("Unable to get payment summary facts for userId: {}, due to bad request", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request, please try valid value");
        } catch (Exception e) {
            log.error("Unable to get payment summary facts for userId: {}", userId);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error, please try again");
        }
    }

    @PostMapping(value = "/delete_payment_card")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deletePaymentCard(@RequestParam long uId, @RequestParam Long cardId) {
        try {
            GetPaymentSummaryFactsResponse response = paymentService.deletePaymentCard(uId, cardId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            log.error("Unable to delete payment card for userId: {}, due to bad request", uId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request, please try valid value");
        } catch (Exception e) {
            log.error("Unable to delete payment card for userId: {}, with cardId: {}", uId, cardId);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error, please try again");
        }
    }
}
