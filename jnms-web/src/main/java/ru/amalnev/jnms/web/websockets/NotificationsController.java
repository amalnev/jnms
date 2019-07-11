package ru.amalnev.jnms.web.websockets;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotificationsController
{
    @GetMapping("/notifications")
    private String showNotifications(final Model uiModel)
    {
        uiModel.addAttribute("viewType", "notifications");
        return "main-view";
    }
}
