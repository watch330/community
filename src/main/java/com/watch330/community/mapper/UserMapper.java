package com.watch330.community.mapper;

import com.watch330.community.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,avatar_url) " +
            "values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    public void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);

    //todo 用户登录-验证新接口是否正确
    @Update("update user set token=#{token},  gmt_modified=#{modifiedTime}, avatar_url=#{avatar}, name=#{name}, bio=#{bio}" +
            "where account_id = #{id}")
    public void loginUpdate(@Param("id") String id, @Param("token") String token, @Param("modifiedTime") Long time, @Param("avatar") String avatar, @Param("name") String name, @Param("bio") String bio);

    @Select("select * from user where name=#{name}")
    User findByName(@Param("name") String name);

    @Select("select * from user where account_id = #{id}")
    User findById(@Param("id") String id);
}
