package com.bitforce.tuteme.dto.ServiceResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPaymentsResponse {
    List<Payment> payments = new ArrayList<>();
    private int total;
    private int current;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payment {
        private long id;
        private String paymentType;
        private String paymentMethod;
        private BigDecimal amount;
        private LocalDateTime paymentAt;
        private String tutorName;
        private String course;
        private long courseId;
    }
}
