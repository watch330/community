package com.watch330.community.Controller;

import com.watch330.community.dto.QuestionDTO;
import com.watch330.community.dto.ShowCommentDTO;
import com.watch330.community.enums.CommentType;
import com.watch330.community.service.CommentService;
import com.watch330.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id,
                           Model model) {
        QuestionDTO questionDTO = questionService.findById(id);
        questionService.increView(id);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        model.addAttribute("question", questionDTO);
        model.addAttribute("relatedQuestions",relatedQuestions);

        List<ShowCommentDTO> list = commentService.getCommentList(id,CommentType.QUESTION);
        model.addAttribute("fistComment",list);

        return "question";
    }
}
