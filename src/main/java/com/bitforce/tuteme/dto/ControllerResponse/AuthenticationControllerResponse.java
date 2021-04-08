package com.bitforce.tuteme.dto.ControllerResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationControllerResponse {
    private String token;
    private int expirationInMilliseconds;
    private Long userId;
    private String email;
    private Long profileId;
    private String role;
}
