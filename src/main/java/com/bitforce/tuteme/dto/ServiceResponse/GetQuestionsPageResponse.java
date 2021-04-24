package com.bitforce.tuteme.dto.ServiceResponse;

import com.bitforce.tuteme.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetQuestionsPageResponse {
    List<Question> questionList = new ArrayList<>();
    private int total;
    private int current;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Question {
        private Long id;
        private String title;
        private String content;
        private LocalDateTime createdAt;
        private List<Tag> tags;
        private String userName;
        private byte[] userImg;
        private int votes;
        private int answers;
    }
}
