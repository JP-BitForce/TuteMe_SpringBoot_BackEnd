package com.bitforce.tuteme.dto.ControllerResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentsControllerResponse {
    List<Comment> commentList = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Comment {
        private Long id;
        private Long blogId;
        private Long authorId;
        private String author;
        private String comment;
        private LocalDateTime date;
        private byte[] authorImg;
        private List<Reply> replyList;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Reply {
            private Long id;
            private Long authorId;
            private String author;
            private String reply;
            private LocalDateTime date;
            private byte[] authorImg;
        }
    }
}
