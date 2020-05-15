package com.watch330.community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.watch330.community.dto.QuestionDTO;
import com.watch330.community.mapper.QuestionMapper;
import com.watch330.community.mapper.UserMapper;
import com.watch330.community.model.Question;
import com.watch330.community.model.QuestionExample;
import com.watch330.community.model.User;
import com.watch330.community.model.UserExample;
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
        QuestionExample questionExample = new QuestionExample();
        PageHelper.startPage(pageNum, pageSize);
        List<Question> questions = questionMapper.selectByExample(questionExample);
        return getPageInfo(questions);
    }

    public PageInfo getListByUserId(Integer pageNum, Integer pageSize, String id) {
        //使用example设置查询条件
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(id);

        PageHelper.startPage(pageNum, pageSize);
        List<Question> questions = questionMapper.selectByExample(questionExample);
        return getPageInfo(questions);
    }

    @NotNull
    private PageInfo getPageInfo(List<Question> questions) {
        PageInfo tempPageInfo = new PageInfo<>(questions, 7);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            UserExample userExample = new UserExample();
            userExample.createCriteria()
                    .andAccountIdEqualTo(question.getCreator());
            List<User> users = userMapper.selectByExample(userExample);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(users.get(0));
            questionDTOList.add(questionDTO);
        }
        PageInfo pageInfo = new PageInfo<>(questionDTOList);
        BeanUtils.copyProperties(tempPageInfo, pageInfo, "list");
        return pageInfo;
    }

    public QuestionDTO findById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question==null){
            return null;
        }
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(question.getCreator());
        List<User> users = userMapper.selectByExample(userExample);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setUser(users.get(0));

        return questionDTO;
    }

    public boolean createOrUpdate(Question question, Integer editId) {

        if(editId!=null){
            Question updateQuestion = new Question();
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setGmtModified(System.currentTimeMillis());

            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(editId);

            questionMapper.updateByExampleSelective(updateQuestion,questionExample);
            return true;
        }else{
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
            return false;
        }
    }
}
