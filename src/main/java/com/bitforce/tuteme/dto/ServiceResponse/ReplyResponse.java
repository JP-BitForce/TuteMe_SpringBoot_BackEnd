package com.bitforce.tuteme.dto.ServiceResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyResponse {
    private Long id;
    private Long authorId;
    private String author;
    private String reply;
    private LocalDateTime date;
    private byte[] authorImg;
}
