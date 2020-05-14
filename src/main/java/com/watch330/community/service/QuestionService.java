package com.watch330.community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.watch330.community.dto.QuestionDTO;
import com.watch330.community.mapper.QuestionMapper;
import com.watch330.community.mapper.UserMapper;
import com.watch330.community.model.Question;
import com.watch330.community.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    public PageInfo getAllList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Question> questions = questionMapper.getQuestionList();
        return getPageInfo(questions);
    }

    public PageInfo getListByUserId(Integer pageNum, Integer pageSize, String id) {
        PageHelper.startPage(pageNum, pageSize);
        List<Question> questions = questionMapper.getByUserId(id);
        return getPageInfo(questions);
    }

    @NotNull
    private PageInfo getPageInfo(List<Question> questions) {
        PageInfo tempPageInfo = new PageInfo<>(questions, 7);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        PageInfo pageInfo = new PageInfo<>(questionDTOList);
        BeanUtils.copyProperties(tempPageInfo, pageInfo, "list");
        return pageInfo;
    }

    public QuestionDTO findById(Integer id) {
        Question question = questionMapper.findById(id);
        if (question==null){
            return null;
        }
        User user= userMapper.findById(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setUser(user);

        return questionDTO;
    }

    public boolean createOrUpdate(Question question, Integer editId) {

        if(editId!=null){
            questionMapper.updateQuestion(editId,question.getTitle(),question.getDescription(),question.getTag(),System.currentTimeMillis());
            return true;
        }else{
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
            return false;
        }
    }
}
