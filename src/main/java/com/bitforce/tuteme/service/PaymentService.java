package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.ServiceRequest.EnrollCourseAndPayRequest;
import com.bitforce.tuteme.dto.ServiceResponse.GetPaymentSummaryFactsResponse;
import com.bitforce.tuteme.dto.ServiceResponse.GetPaymentsResponse;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.model.*;
import com.bitforce.tuteme.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final Logger log = LoggerFactory.getLogger(OneStepService.class);

    private final PaymentRepository paymentRepository;
    private final BankPaymentRepository bankPaymentRepository;
    private final PaypalRepository paypalRepository;
    private final UserRepository userRepository;
    private final PaymentPlanRepository paymentPlanRepository;
    private final PaymentCardRepository paymentCardRepository;
    private final FileStorageService fileStorageService = new FileStorageService("BankSlips");

    public PaymentService(PaymentRepository paymentRepository,
                          BankPaymentRepository bankPaymentRepository,
                          PaypalRepository paypalRepository,
                          UserRepository userRepository,
                          PaymentPlanRepository paymentPlanRepository,
                          PaymentCardRepository paymentCardRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.bankPaymentRepository = bankPaymentRepository;
        this.paypalRepository = paypalRepository;
        this.userRepository = userRepository;
        this.paymentPlanRepository = paymentPlanRepository;
        this.paymentCardRepository = paymentCardRepository;
    }

    public String getBankSlipUrl(MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/courses/uploads/BankSlips/")
                .path(fileName)
                .toUriString();
    }

    public Payment createNewPayment(EnrollCourseAndPayRequest request, Course course, User user) {
        Payment payment = Payment
                .builder()
                .paymentMethod(request.getPaymentMethod())
                .PaymentType(request.getPaymentType())
                .user(user)
                .paymentAt(LocalDateTime.now())
                .amount(round(new BigDecimal(request.getAmount()), 2))
                .course(course)
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
            savePaymentCard(request.getCardNo(), user, request.getCardType());
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
        log.info("created payment with id:{}", payment.getId());
        return payment;
    }

    public GetPaymentsResponse getPayments(Long userId, int page) throws EntityNotFoundException {
        if (!userRepository.findById(userId).isPresent()) {
            throw new EntityNotFoundException("ENTITY_NOT_FOUND");
        }
        User user = userRepository.findById(userId).get();

        Page<Payment> payments = paymentRepository.findAllByUser(user, PageRequest.of(page, 10));
        log.info("No of payment pages found for userId: {} was => {}", userId, payments.getTotalElements());
        return new GetPaymentsResponse(
                payments.stream().map(payment -> new GetPaymentsResponse.Payment(
                        payment.getId(),
                        payment.getPaymentType(),
                        payment.getPaymentMethod(),
                        payment.getAmount(),
                        payment.getPaymentAt(),
                        getUserFullName(payment.getCourse().getTutor().getUser()),
                        payment.getCourse().getName(),
                        payment.getCourse().getId()
                )).collect(Collectors.toList()),
                payments.getTotalPages(),
                payments.getNumber()
        );
    }

    public String upgradePlan(Long userId, String plan) throws EntityNotFoundException {
        User user = getUser(userId);
        PaymentPlan paymentPlan = paymentPlanRepository.findByUser(user);
        if (paymentPlan == null) {
            PaymentPlan newPlan = PaymentPlan
                    .builder()
                    .plan(plan)
                    .user(user)
                    .build();
            paymentPlanRepository.save(newPlan);
        } else {
            paymentPlan.setPlan(plan);
            paymentPlanRepository.save(paymentPlan);
        }
        return "Payment plan updated successfully!";
    }

    public String getPaymentPlanByUser(Long userId) throws EntityNotFoundException {
        User user = getUser(userId);
        PaymentPlan paymentPlan = paymentPlanRepository.findByUser(user);
        if (paymentPlan == null) {
            throw new EntityNotFoundException("PAYMENT_PLAN_NOT_FOUND");
        }
        return paymentPlan.getPlan();
    }

    public GetPaymentSummaryFactsResponse getPaymentSummaryFacts(Long userId) throws EntityNotFoundException {
        String plan = getPaymentPlanByUser(userId);
        List<String> bankAccounts = new ArrayList<>();
        bankAccounts.add("1234567890");
        List<PaymentCard> paymentCards = paymentCardRepository.findAllByUser(getUser(userId));
        return new GetPaymentSummaryFactsResponse(
                plan,
                bankAccounts,
                paymentCards.stream().map(paymentCard -> new GetPaymentSummaryFactsResponse.Card(
                        paymentCard.getId(),
                        paymentCard.getCardNo(),
                        paymentCard.getType()
                )).collect(Collectors.toList())
        );
    }

    public GetPaymentSummaryFactsResponse deletePaymentCard(Long userId, Long cardId) throws EntityNotFoundException {
        String plan = getPaymentPlanByUser(userId);
        List<String> bankAccounts = new ArrayList<>();
        bankAccounts.add("1234567890");
        PaymentCard paymentCard = paymentCardRepository.findByCardNo(cardId);
        if (paymentCard == null) {
            log.error("payment card not found for id:{}", cardId);
            throw new EntityNotFoundException("PAYMENT_CARD_NOT_FOUND");
        }
        paymentCardRepository.delete(paymentCard);
        List<PaymentCard> paymentCards = paymentCardRepository.findAllByUser(getUser(userId));
        return new GetPaymentSummaryFactsResponse(
                plan,
                bankAccounts,
                paymentCards.stream().map(card -> new GetPaymentSummaryFactsResponse.Card(
                        card.getId(),
                        card.getCardNo(),
                        card.getType()
                )).collect(Collectors.toList())
        );
    }

    private void savePaymentCard(String cardNo, User user, String type) {
        Long card = Long.parseLong(cardNo);
        PaymentCard paymentCard = paymentCardRepository.findByCardNo(card);
        if (paymentCard == null) {
            PaymentCard newCard = PaymentCard
                    .builder()
                    .cardNo(card)
                    .type(type)
                    .user(user)
                    .build();
            paymentCardRepository.save(newCard);
        }
    }

    private User getUser(Long userId) throws EntityNotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new EntityNotFoundException("USER_NOT_FOUND");
        }
        return userOptional.get();
    }

    private LocalDateTime parse(String date) {
        return LocalDateTime.parse(date);
    }

    public static BigDecimal round(BigDecimal d, int decimalPlace) {
        return d.setScale(decimalPlace, RoundingMode.HALF_EVEN);
    }

    private String getUserFullName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}
