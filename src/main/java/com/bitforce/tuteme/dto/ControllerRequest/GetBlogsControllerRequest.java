package com.bitforce.tuteme.dto.ControllerRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetBlogsControllerRequest {
    private String userId;
    private int page;
}
