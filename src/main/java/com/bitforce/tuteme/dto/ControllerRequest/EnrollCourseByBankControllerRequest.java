package com.bitforce.tuteme.dto.ControllerRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollCourseByBankControllerRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String userId;
    private String courseId;
    private String paymentType;
    private String depositedAt;
    private String amount;
}
