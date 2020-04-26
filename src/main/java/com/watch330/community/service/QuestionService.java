package com.watch330.community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.watch330.community.dto.QuestionDTO;
import com.watch330.community.mapper.QuestionMapper;
import com.watch330.community.mapper.UserMapper;
import com.watch330.community.model.Question;
import com.watch330.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    public PageInfo list(Integer pageNum, Integer pageSize) {
        if (pageNum <= 0)
            pageNum = 1;
        PageHelper.startPage(pageNum, pageSize);
        List<Question> questions = questionMapper.getQuestionList();
        PageInfo tempPageInfo = new PageInfo<>(questions,7);
        if (tempPageInfo.getSize()==0) {
            tempPageInfo = list(tempPageInfo.getPages(), pageSize);
            return tempPageInfo;
        }
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        PageInfo pageInfo = new PageInfo<>(questionDTOList);
        BeanUtils.copyProperties(tempPageInfo,pageInfo,"list");
        return pageInfo;
    }
}
