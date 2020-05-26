package com.watch330.community.Controller;

import com.watch330.community.mapper.QuestionMapper;
import com.watch330.community.model.Question;
import com.watch330.community.model.User;
import com.watch330.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    @GetMapping("/publish/{id}")
    public String modifyQuestion(Model model, @PathVariable Long id,
                                 HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        Question question = questionMapper.selectByPrimaryKey(id);
        if (!Objects.equals(user.getAccountId(), question.getCreator()))
            return "index";
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        request.getSession().setAttribute("questionEditId", id);

        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,
            Model model
    ) {

        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);

        if (title == null || Objects.equals(title, "")) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || Objects.equals(description, "")) {
            model.addAttribute("error", "问题描述不能为空");
            return "publish";
        }
        if (tag == null || Objects.equals(tag, "")) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getAccountId());


       if( questionService.createOrUpdate(question,(Long) request.getSession().getAttribute("questionEditId")))
           request.getSession().setAttribute("questionEditId",null);

        return "redirect:/";
    }


//    @PostMapping("/publish/{id}")
//    public String updateQuestion(@PathVariable Integer id,
//                                 @RequestParam("title") String title,
//                                 @RequestParam("description") String description,
//                                 @RequestParam("tag") String tag){
//        questionMapper.updateQuestion(id,title,description,tag);
//        return "redirect:/question/" + id;
//    }

}
