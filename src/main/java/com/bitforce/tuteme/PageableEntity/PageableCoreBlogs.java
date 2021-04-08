package com.bitforce.tuteme.PageableEntity;

import com.bitforce.tuteme.model.Blog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableCoreBlogs {
    private List<Blog> blog = new ArrayList<>();
    private int total;
    private int current;
}
