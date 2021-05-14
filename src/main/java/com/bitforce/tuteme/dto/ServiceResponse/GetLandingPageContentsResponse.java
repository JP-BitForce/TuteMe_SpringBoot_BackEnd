package com.bitforce.tuteme.dto.ServiceResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetLandingPageContentsResponse {
    Map<String, Object> feedbacks = new HashMap<>();
    Map<String, Object> counts = new HashMap<>();
}
