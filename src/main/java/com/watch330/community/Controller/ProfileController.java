package com.watch330.community.Controller;

import com.github.pagehelper.PageInfo;
import com.watch330.community.mapper.UserMapper;
import com.watch330.community.model.User;
import com.watch330.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          Model model,
                          @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                          @PathVariable String action) {

        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            model.addAttribute("path", "/profile/questions.getList");
        } else if ("replies".equals(action)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }

        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            return "redirect:/";
        }

        Integer pageSize = 10;
        PageInfo pageInfo = questionService.getListByUserId(pageNum, pageSize, user.getAccountId());
        model.addAttribute("pageInfo", pageInfo);
        return "profile";
    }

    @RequestMapping("/profile/questions.getList")
    @ResponseBody
    public ModelAndView profileGetList(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                       HttpServletRequest request) {

        Integer pageSize = 10;
        User user = (User) request.getSession().getAttribute("user");
        PageInfo pageInfo = questionService.getListByUserId(pageNum, pageSize, user.getAccountId());

        ModelAndView modelAndView = new ModelAndView("profile");


        modelAndView.addObject("pageInfo", pageInfo);
        modelAndView.addObject("path", "/profile/questions.getList");
        modelAndView.addObject("section", "questions");
        modelAndView.addObject("sectionName", "我的提问");

        return modelAndView;
    }
}
