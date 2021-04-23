package com.bitforce.tuteme.PageableEntity;

import com.bitforce.tuteme.model.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableCoreQuestions {
    private List<Question> questions = new ArrayList<>();
    private int total;
    private int current;
}
