package com.watch330.community.Controller;

import com.github.pagehelper.PageInfo;
import com.watch330.community.mapper.QuestionMapper;
import com.watch330.community.mapper.UserMapper;
import com.watch330.community.model.User;
import com.watch330.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          Model model,
                          @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                          @PathVariable String action) {

        if("questions".equals(action)){
           model.addAttribute("section","questions");
           model.addAttribute("sectionName","我的提问");
        } else if ("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }
        Cookie[] cookies = request.getCookies();

        User user = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        if (user == null) {
            return "redirect:/";
        }

        Integer pageSize = 10;
        PageInfo pageInfo = questionService.getListByUserId(pageNum, pageSize, user.getAccountId());
        model.addAttribute("pageInfo", pageInfo);
        return "profile";
    }
}
