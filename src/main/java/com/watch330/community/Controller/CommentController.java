package com.watch330.community.Controller;

import com.watch330.community.dto.CommentDTO;
import com.watch330.community.dto.ResultDTO;
import com.watch330.community.exception.ErrorCode;
import com.watch330.community.model.Comment;
import com.watch330.community.model.User;
import com.watch330.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object postComment(@RequestBody CommentDTO commentDTO,
                              HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");

        if(user == null){
            return ResultDTO.errorOf(ErrorCode.USER_NOT_LOGIN);
        }

        Comment comment = new Comment();

        comment.setContent(commentDTO.getContent());
        comment.setParentId(commentDTO.getParentId());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentCreator(user.getId());

        commentService.insert(comment);

        return ResultDTO.success();
    }
}
