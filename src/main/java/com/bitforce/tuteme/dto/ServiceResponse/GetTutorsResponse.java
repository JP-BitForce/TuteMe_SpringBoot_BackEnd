package com.bitforce.tuteme.dto.ServiceResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTutorsResponse {
    List<Tutor> tutorList = new ArrayList<>();
    private int total;
    private int current;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tutor {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String gender;
        private byte[] src;
        private String city;
        private String district;
        private String description;
        private Double rating;
    }
}
