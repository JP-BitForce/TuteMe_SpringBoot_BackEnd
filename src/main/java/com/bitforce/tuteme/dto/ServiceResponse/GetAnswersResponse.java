package com.bitforce.tuteme.dto.ServiceResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAnswersResponse {
    List<Answer> answers = new ArrayList<>();
    private boolean currentUserVotedForQuestion;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Answer {
        private Long id;
        private String content;
        private LocalDateTime createdAt;
        private int votes;
        private String userName;
        private byte[] userImg;
    }
}
