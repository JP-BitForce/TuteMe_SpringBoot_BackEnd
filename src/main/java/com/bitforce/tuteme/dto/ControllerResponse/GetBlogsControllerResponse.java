package com.bitforce.tuteme.dto.ControllerResponse;

import com.bitforce.tuteme.model.Comment;
import com.bitforce.tuteme.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetBlogsControllerResponse {
    List<Blog> blogs = new ArrayList<>();
    private int total;
    private int current;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Blog {
        private Long id;
        private String title;
        private String content;
        private Integer likes;
        private LocalDateTime date;
        private String description;
        private String coverImgUrl;
        private List<Comment> comments;
        private User user;
    }
}
