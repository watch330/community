package com.watch330.community.service;

import com.watch330.community.dto.GithubUser;
import com.watch330.community.mapper.UserMapper;
import com.watch330.community.model.User;
import com.watch330.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void loginInsertOrUpdate(GithubUser githubUser, String token) {
        //登陆成功,写入session和cookie
        //先判断用户是否存在,用户不存在就创建新用户插入数据库
        //todo 用户登录-检查逻辑是否正确
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(String.valueOf(githubUser.getId()));
        List<User> users = userMapper.selectByExample(userExample);

        if (users.size() == 0) {
            User user = new User();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatarUrl());
            user.setBio(githubUser.getBio());
            userMapper.insert(user);

        } else {

            User update = new User();
            update.setToken(token);
            update.setGmtModified(System.currentTimeMillis());
            update.setAvatarUrl(githubUser.getAvatarUrl());
            update.setName(githubUser.getName());
            update.setBio(githubUser.getBio());

            UserExample example = new UserExample();
            example.createCriteria()
                    .andAccountIdEqualTo(String.valueOf(githubUser.getId()));
            userMapper.updateByExampleSelective(update, example);

//            userMapper.loginUpdate(String.valueOf(githubUser.getId()), token, System.currentTimeMillis(),
//                    githubUser.getAvatarUrl(), githubUser.getName(), githubUser.getBio());
        }
    }
}
