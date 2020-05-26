package com.watch330.community.service;

import com.watch330.community.enums.CommentType;
import com.watch330.community.exception.ErrorCode;
import com.watch330.community.exception.CustomizeException;
import com.watch330.community.mapper.CommentMapper;
import com.watch330.community.mapper.QuestionExtMapper;
import com.watch330.community.mapper.QuestionMapper;
import com.watch330.community.model.Comment;
import com.watch330.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(ErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentType.isExist(comment.getType())) {
            throw new CustomizeException(ErrorCode.TYPE_PARAM_NOT_FOUND);
        }

        if (Objects.equals(comment.getType(), CommentType.COMMENT.getType())) {
            Comment parentComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (parentComment == null)
                throw new CustomizeException(ErrorCode.COMMENT_NOT_FOUND);
            else {
                commentMapper.insertSelective(comment);
                questionExtMapper.incComment(parentComment.getParentId());
            }
        } else {
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null)
                throw new CustomizeException((ErrorCode.QUESTION_NOT_FOUND));
            else {
                commentMapper.insertSelective(comment);
                questionExtMapper.incComment(comment.getParentId());
            }
        }

    }
}
