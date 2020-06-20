package com.watch330.community.Controller;

import com.watch330.community.dto.NotificationDTO;
import com.watch330.community.enums.NotificationType;
import com.watch330.community.model.User;
import com.watch330.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String getNotification(@PathVariable(name = "id") Long id,
                                  Model model,
                                  HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            return "redirect:/";
        }

        NotificationDTO notificationDTO = notificationService.readNotification(id, user.getId());
        if (NotificationType.REPLY_COMMENT.getType() == notificationDTO.getType() || NotificationType.REPLY_QUESTION.getType() == notificationDTO.getType())
            return "redirect:/question/" + notificationDTO.getOuterId();
        else
            return "redirect:/";
    }

}
