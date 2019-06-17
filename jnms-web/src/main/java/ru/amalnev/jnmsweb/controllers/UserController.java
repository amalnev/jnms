package ru.amalnev.jnmsweb.controllers;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.amalnev.jnmscommon.entities.security.User;
import ru.amalnev.jnmscommon.repositories.IUserRepository;
import ru.amalnev.jnmscommon.services.SecurityService;
import ru.amalnev.jnmsweb.constants.Constants;

@Controller
@RequestMapping("/users")
public class UserController implements ApplicationContextAware
{
    @Setter
    private ApplicationContext applicationContext;

    @Setter(onMethod=@__({@Autowired}))
    private SecurityService securityService;

    @GetMapping
    private String show(final Model uiModel)
    {
        uiModel.addAttribute("springContext", applicationContext);
        uiModel.addAttribute("viewType", "userGrid");
        uiModel.addAttribute("users", securityService.findAllUsers());

        return Constants.MAIN_VIEW;
    }

    @GetMapping("/user")
    private String createOrEdit(final Model uiModel,
                                final @RequestParam Long userId)
    {
        final User user = securityService.findUserById(userId).orElse(new User());

        uiModel.addAttribute("springContext", applicationContext);
        uiModel.addAttribute("viewType", "userForm");
        uiModel.addAttribute("user", user);

        return Constants.MAIN_VIEW;
    }

    @PostMapping("/save")
    private String save(final Model uiModel,
                        final @ModelAttribute User user)
    {
        securityService.saveUser(user);

        return "redirect:/users";
    }

    @GetMapping("/delete")
    private String delete(final Model uiModel,
                          final @RequestParam Long userId)
    {
        securityService.deleteUserById(userId);
        return "redirect:/users";
    }
}
