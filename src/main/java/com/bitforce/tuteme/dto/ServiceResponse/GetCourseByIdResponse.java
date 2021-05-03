package com.bitforce.tuteme.dto.ServiceResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCourseByIdResponse {
    private Long id;
    private String title;
    private String description;
    private String duration;
    private String tutorName;
    private byte[] courseImg;
    private double rating;
    private BigDecimal price;
    private Long tutorId;
}
