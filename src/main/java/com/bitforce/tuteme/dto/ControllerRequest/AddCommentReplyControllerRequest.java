package com.bitforce.tuteme.dto.ControllerRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentReplyControllerRequest {
    private String userId;
    private String blogId;
    private String commentParentId;
    private String reply;
}
