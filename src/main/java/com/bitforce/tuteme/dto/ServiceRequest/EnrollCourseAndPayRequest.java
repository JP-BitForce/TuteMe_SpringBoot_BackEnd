package com.bitforce.tuteme.dto.ServiceRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollCourseAndPayRequest {
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
    private String depositedAt;
    private MultipartFile formData;
    private String paymentMethod;
    private String userId;
    private String courseId;
    private String paymentType;
}
