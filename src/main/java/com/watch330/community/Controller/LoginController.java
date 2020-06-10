package com.watch330.community.Controller;

import com.watch330.community.mapper.UserMapper;
import com.watch330.community.model.User;
import com.watch330.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/login")
    public String login(){

        return "login";
    }

    //TODO 密码验证
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String loginByUserName(@RequestParam(name = "userName") String userName,
                                  @RequestParam(name = "passWord") String passWord,
                                  HttpServletResponse response){
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andNameEqualTo(userName);
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() == 0)
            return "redirect:/login";
        Cookie cookie = new Cookie("token", users.get(0).getToken());
        cookie.setMaxAge(4592000);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
