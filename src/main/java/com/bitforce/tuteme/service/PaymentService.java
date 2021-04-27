package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.ServiceRequest.EnrollCourseAndPayRequest;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.model.BankPayment;
import com.bitforce.tuteme.model.Payment;
import com.bitforce.tuteme.model.PaypalPayment;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.repository.BankPaymentRepository;
import com.bitforce.tuteme.repository.PaymentRepository;
import com.bitforce.tuteme.repository.PaypalRepository;
import com.bitforce.tuteme.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@Service
public class PaymentService {
    private final Logger log = LoggerFactory.getLogger(OneStepService.class);

    private final PaymentRepository paymentRepository;
    private final BankPaymentRepository bankPaymentRepository;
    private final PaypalRepository paypalRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService = new FileStorageService("BankSlips");

    public PaymentService(PaymentRepository paymentRepository,
                          BankPaymentRepository bankPaymentRepository,
                          PaypalRepository paypalRepository,
                          UserRepository userRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.bankPaymentRepository = bankPaymentRepository;
        this.paypalRepository = paypalRepository;
        this.userRepository = userRepository;
    }

    public String getBankSlipUrl(MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/courses/uploads/BankSlips/")
                .path(fileName)
                .toUriString();
    }

    public void createNewPayment(EnrollCourseAndPayRequest request) throws EntityNotFoundException {
        Long uId = Long.parseLong(request.getUserId());
        if (!userRepository.findById(uId).isPresent()) {
            log.error("user not for found id: {}", uId);
            throw new EntityNotFoundException("USER_NOT_FOUND");
        }
        User user = userRepository.findById(uId).get();
        Payment payment = Payment
                .builder()
                .paymentMethod(request.getPaymentMethod())
                .PaymentType(request.getPaymentType())
                .user(user)
                .build();
        paymentRepository.save(payment);
        if (request.getPaymentMethod().equals("paypal")) {
            PaypalPayment paypalPayment = PaypalPayment
                    .builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .address(request.getAddress())
                    .email(request.getEmail())
                    .city(request.getCity())
                    .zip(request.getZip())
                    .mobile(request.getMobile())
                    .cvv(request.getCvv())
                    .exp(request.getExp())
                    .cardNo(request.getCardNo())
                    .date(LocalDateTime.now())
                    .payment(payment)
                    .build();
            paypalRepository.save(paypalPayment);
        } else {
            BankPayment bankPayment = BankPayment
                    .builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .depositedAt(parse(request.getDepositedAt()))
                    .depositSlipUrl(getBankSlipUrl(request.getFormData()))
                    .payment(payment)
                    .email(request.getEmail())
                    .build();
            bankPaymentRepository.save(bankPayment);
        }
    }

    private LocalDateTime parse(String date) {
        return LocalDateTime.parse(date);
    }
}
