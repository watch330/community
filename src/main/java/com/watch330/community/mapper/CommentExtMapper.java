package com.watch330.community.mapper;

import com.watch330.community.model.Comment;

public interface CommentExtMapper {
    int incComment(Long id);

    int decCommentCount(Comment comment);
}
