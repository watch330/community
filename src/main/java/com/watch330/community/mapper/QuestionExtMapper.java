package com.watch330.community.mapper;

import com.watch330.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Long id);

    int incComment(Long id);

    int decComment(Question question);

    List<Question> selectRelated(Question question);
}
