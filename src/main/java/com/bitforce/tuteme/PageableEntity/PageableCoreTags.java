package com.bitforce.tuteme.PageableEntity;

import com.bitforce.tuteme.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableCoreTags {
    private List<Tag> tags = new ArrayList<>();
    private int total;
    private int current;
}
