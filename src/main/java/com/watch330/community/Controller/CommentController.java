package com.watch330.community.Controller;

import com.watch330.community.dto.CommentCreateDTO;
import com.watch330.community.dto.QuestionDTO;
import com.watch330.community.dto.ResultDTO;
import com.watch330.community.dto.ShowCommentDTO;
import com.watch330.community.enums.CommentType;
import com.watch330.community.exception.ErrorCode;
import com.watch330.community.mapper.CommentMapper;
import com.watch330.community.mapper.QuestionMapper;
import com.watch330.community.model.Comment;
import com.watch330.community.model.User;
import com.watch330.community.service.CommentService;
import com.watch330.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentMapper commentMapper;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object postComment(@RequestBody CommentCreateDTO commentDTO,
                              HttpServletRequest request) {

        if (commentDTO == null || StringUtils.isBlank(commentDTO.getContent()))
            return ResultDTO.errorOf(ErrorCode.CONTENT_IS_EMPTY);
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            return ResultDTO.errorOf(ErrorCode.USER_NOT_LOGIN);
        }

        Comment comment = new Comment();

        comment.setContent(commentDTO.getContent());
        comment.setParentId(commentDTO.getParentId());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentCreator(user.getId());

        commentService.insert(comment,user);

        return ResultDTO.success();
    }

    @RequestMapping("/getCommentList")
    @ResponseBody
    public ModelAndView getCommentList(@RequestParam(name = "id") Long id) {

        ModelAndView modelAndView = new ModelAndView("question");

        List<ShowCommentDTO> questionCommentList = commentService.getCommentList(id, CommentType.QUESTION);
        QuestionDTO questionDTO = questionService.findById(id);


        modelAndView.addObject("question", questionDTO);
        modelAndView.addObject("fistComment", questionCommentList);

        return modelAndView;
    }

    @GetMapping("/getSecondComment")
    public String getSecondComment() {
        return "getSecondComment";
    }



    @RequestMapping(value = "/comments", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView get2ndCommentList(@RequestParam(name = "id") Long id) {
        ModelAndView modelAndView = new ModelAndView("getSecondComment");
        List<ShowCommentDTO> secondCommentList = commentService.getCommentList(id, CommentType.COMMENT);

        modelAndView.addObject("parentId", id);
        modelAndView.addObject("secondComment", secondCommentList);
        return modelAndView;
    }


    @RequestMapping("/updateNavi")
    @ResponseBody
    public ModelAndView getLoginUpdate() {
        return new ModelAndView("NaviBarUpdate");

    }

//    public ModelAndView test(){
//
//    }

    @RequestMapping(value = "/deleteComment",method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView deleteComment(@RequestParam(name = "id") Long id,
                                      @RequestParam(name = "parentId") Long parentId,
                                      @RequestParam(name = "type") int type) {

        if (type == 1) {
            commentService.deleteRootComment(id);
            return null;
        } else {
            commentService.deleteChildComment(id);

            Comment comment = commentMapper.selectByPrimaryKey(parentId);

            List<ShowCommentDTO> questionCommentList = commentService.getCommentList(comment.getParentId(), CommentType.QUESTION);
            QuestionDTO questionDTO = questionService.findById(comment.getParentId());

            ModelAndView modelAndView = new ModelAndView("question");

            modelAndView.addObject("question", questionDTO);
            modelAndView.addObject("fistComment", questionCommentList);
            return modelAndView;
        }



    }
}
