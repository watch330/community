package com.watch330.community.Controller;

import com.github.pagehelper.PageInfo;
import com.watch330.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class GreetingController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String greeting(HttpServletRequest request,
                           Model model,
                           @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum) {

        Integer pageSize = 10;
        PageInfo pageInfo = questionService.getAllList(pageNum, pageSize);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("path","/getPageInfo");


        return "index";
    }

    @RequestMapping("/getPageInfo")
    @ResponseBody
    public ModelAndView getPageInfo(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                    HttpServletRequest request){
        Integer pageSize = 10;
        PageInfo pageInfo = questionService.getAllList(pageNum, pageSize);

        ModelAndView modelAndView = new ModelAndView("index");

        modelAndView.addObject("pageInfo",pageInfo);
        modelAndView.addObject("path","/getPageInfo");

        return modelAndView;
    }
}
