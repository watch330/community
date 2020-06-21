package com.watch330.community.Controller;

import com.watch330.community.cache.TagCache;
import com.watch330.community.mapper.QuestionMapper;
import com.watch330.community.model.Question;
import com.watch330.community.model.User;
import com.watch330.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
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
    public String publish(Model model) {
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @GetMapping("/publish/{id}")
    public String modifyQuestion(Model model, @PathVariable Long id,
                                 HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        Question question = questionMapper.selectByPrimaryKey(id);
        if (!Objects.equals(user.getId(), question.getCreator()))
            return "redirect:/";
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("tags", TagCache.get());
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
        model.addAttribute("tags", TagCache.get());

        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }

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

        String inValidTags = TagCache.isValid(tag);
        if (StringUtils.isNotBlank(inValidTags)) {
            model.addAttribute("error", "含有非法标签:" + inValidTags);
            return "publish";
        }


        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());


        if (questionService.createOrUpdate(question, (Long) request.getSession().getAttribute("questionEditId")))
            request.getSession().setAttribute("questionEditId", null);

        return "redirect:/";
    }


}
