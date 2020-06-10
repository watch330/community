package com.watch330.community.service;

import com.watch330.community.dto.ShowCommentDTO;
import com.watch330.community.enums.CommentType;
import com.watch330.community.exception.ErrorCode;
import com.watch330.community.exception.CustomizeException;
import com.watch330.community.mapper.*;
import com.watch330.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Transactional
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
                commentExtMapper.incComment(comment.getParentId());
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

    public List<ShowCommentDTO> getCommentList(Long id, CommentType type) {
//        List<ShowCommentDTO> tempList = new ArrayList<>();

        //查找问题回复评论
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andTypeEqualTo(type.getType())
                .andParentIdEqualTo(id);
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExampleWithBLOBs(commentExample);

        if (comments.size() == 0) {
            return new ArrayList<>();
        }

        //获得去重的评论人
        List<Integer> creators = comments.stream().map(comment -> comment.getCommentCreator()).distinct().collect(Collectors.toList());

        //获得评论人的信息, 构建评论人map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(creators);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //将评论加上评论人信息
        List<ShowCommentDTO> showCommentDTOS = comments.stream().map(comment -> {
            ShowCommentDTO showCommentDTO = new ShowCommentDTO();
            BeanUtils.copyProperties(comment, showCommentDTO);
            showCommentDTO.setUser(userMap.get(comment.getCommentCreator()));
            return showCommentDTO;
        }).collect(Collectors.toList());

        return showCommentDTOS;

//        for (Comment comment : list) {
//            ShowCommentDTO showCommentDTO = new ShowCommentDTO();
//            BeanUtils.copyProperties(comment, showCommentDTO);
//            showCommentDTO.setUser(userMapper.selectByPrimaryKey(comment.getCommentCreator()));
//            tempList.add(showCommentDTO);
//        }
//        return tempList;

    }

    public List<Comment> get2ndCommentList(Long id) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id);
        return commentMapper.selectByExample(commentExample);
    }

    @Transactional
    public void deleteRootComment(Long id) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(2);
        int deleteRows = commentMapper.deleteByExample(commentExample);

        Question question = new Question();
        question.setId(commentMapper.selectByPrimaryKey(id).getParentId());
        question.setCommentCount(deleteRows+1);

        questionExtMapper.decComment(question);
        commentMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    public void deleteChildComment(Long id){
        //找到当前评论,其父评论的评论数量减少
        Comment comment = commentMapper.selectByPrimaryKey(id);
        comment.setCommentCount(1);
        commentExtMapper.decCommentCount(comment);

        //找到评论的父(即便)
        Comment comment1 = commentMapper.selectByPrimaryKey(comment.getParentId());
        Question question = new Question();
        question.setId(comment1.getParentId());
        question.setCommentCount(1);

        questionExtMapper.decComment(question);
        commentMapper.deleteByPrimaryKey(id);

    }
}
