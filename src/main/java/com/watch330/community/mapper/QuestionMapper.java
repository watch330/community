package com.watch330.community.mapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.watch330.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface QuestionMapper {
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    public void create(Question question);

    @Select("select * from question")
    List<Question> list();

    @Select("select * from question")
    List<Question> getQuestionList();

    @Select("select count(1) from question")
    Integer getTotal();
}
