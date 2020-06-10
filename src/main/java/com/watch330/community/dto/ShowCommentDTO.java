package com.watch330.community.dto;

import com.watch330.community.model.Comment;
import com.watch330.community.model.User;
import lombok.Data;

@Data
public class ShowCommentDTO extends Comment {
    private User user;
}