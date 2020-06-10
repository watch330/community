package com.watch330.community.mapper;

import com.watch330.community.model.Question;

public interface QuestionExtMapper {
    int incView(Long id);
    int incComment(Long id);
    int decComment(Question question);
}
