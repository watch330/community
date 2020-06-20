package com.watch330.community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.watch330.community.dto.QuestionDTO;
import com.watch330.community.exception.CustomizeException;
import com.watch330.community.exception.ErrorCode;
import com.watch330.community.mapper.QuestionExtMapper;
import com.watch330.community.mapper.QuestionMapper;
import com.watch330.community.mapper.UserMapper;
import com.watch330.community.model.Question;
import com.watch330.community.model.QuestionExample;
import com.watch330.community.model.User;
import com.watch330.community.model.UserExample;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        if (StringUtils.isBlank(questionDTO.getTag()))
            return new ArrayList<>();

        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(StringUtils.replace(questionDTO.getTag(), ",", "|"));
        List<Question> relatedQuestions = questionExtMapper.selectRelated(question);

        List<QuestionDTO> questionDTOS = relatedQuestions.stream().map(relatedQuestion -> {
            QuestionDTO dto = new QuestionDTO();
            BeanUtils.copyProperties(relatedQuestion, dto);
            return dto;
        }).collect(Collectors.toList());

        return questionDTOS;
    }

    /**
     * 获得所有问题的分页.
     *
     * @param pageNum  页数
     * @param pageSize 每页的记录数
     * @return 分页信息
     */
    public PageInfo getAllList(Integer pageNum, Integer pageSize) {
        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_modified desc");
        PageHelper.startPage(pageNum, pageSize);
        List<Question> questions = questionMapper.selectByExample(questionExample);
        return getPageInfo(questions);
    }

    /**
     * 根据用户ID, 获得该用户提出的问题分页.
     *
     * @param pageNum  页数
     * @param pageSize 每页的记录数
     * @param id       用户ID
     * @return
     */
    public PageInfo getListByUserId(Integer pageNum, Integer pageSize, Long id) {
        //使用example设置查询条件
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(id);
        questionExample.setOrderByClause("gmt_create desc");

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
                    .andIdEqualTo(question.getCreator());
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

    /**
     * 根据问题ID查找问题
     *
     * @param id 问题ID
     * @return 问题信息
     */
    public QuestionDTO findById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(ErrorCode.QUESTION_NOT_FOUND);
        }
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdEqualTo(question.getCreator());
        List<User> users = userMapper.selectByExample(userExample);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(users.get(0));

        return questionDTO;
    }

    /**
     * 插入或者更新问题
     *
     * @param question 该插入或修改的问题
     * @param editId   需要修改的问题的ID, 可为空
     * @return 是否插入或修改
     */
    public boolean createOrUpdate(Question question, Long editId) {

        if (editId != null) {
            Question updateQuestion = new Question();
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setGmtModified(System.currentTimeMillis());

            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(editId);

            int isUpdate = questionMapper.updateByExampleSelective(updateQuestion, questionExample);
            if (isUpdate != 1) {
                throw new CustomizeException(ErrorCode.QUESTION_NOT_FOUND);
            }
            return true;
        } else {
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            int isInsert = questionMapper.insertSelective(question);
            if (isInsert != 1) {
                throw new CustomizeException(ErrorCode.QUESTION_NOT_FOUND);
            }
            return false;
        }
    }

    public void increView(Long id) {
        questionExtMapper.incView(id);
    }
}
