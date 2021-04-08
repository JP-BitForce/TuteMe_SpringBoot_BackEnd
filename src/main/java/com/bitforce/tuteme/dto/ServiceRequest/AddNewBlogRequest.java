package com.bitforce.tuteme.dto.ServiceRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddNewBlogRequest {
    private String userId;
    private String title;
    private String description;
    private String content;
    private MultipartFile file;
}
