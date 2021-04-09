package com.bitforce.tuteme.dto.ServiceRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentReplyRequest {
    private String userId;
    private String blogId;
    private String commentParentId;
    private String reply;
}
