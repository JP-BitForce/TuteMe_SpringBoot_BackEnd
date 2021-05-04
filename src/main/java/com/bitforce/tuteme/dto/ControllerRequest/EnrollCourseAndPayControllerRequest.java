package com.bitforce.tuteme.dto.ControllerRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollCourseAndPayControllerRequest {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String mobile;
    private String email;
    private String cvv;
    private String exp;
    private String cardNo;
    private String userId;
    private String courseId;
    private String paymentType;
    private String amount;
    private String cardType;
}
