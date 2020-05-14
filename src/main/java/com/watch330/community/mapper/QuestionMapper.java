package com.watch330.community.mapper;

import com.watch330.community.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface QuestionMapper {
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    public void create(Question question);

    @Select("select * from question")
    List<Question> getQuestionList();

    @Select("select * from question where creator=#{id}")
    List<Question> getByUserId(@Param("id") String  id);

    @Select("select * from question where id=#{id}")
    Question findById(@Param("id") Integer id);

    @Update("update question set title=#{title}, description=#{description}, tag=#{tag}, gmt_modified=#{time} where id = #{editId}")
    public void updateQuestion(@Param("editId") Integer id, @Param("title") String title, @Param("description") String description, @Param("tag") String tag,@Param("time") Long time);
}
