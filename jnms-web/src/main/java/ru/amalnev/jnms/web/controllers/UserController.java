package ru.amalnev.jnms.web.controllers;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.amalnev.jnms.common.entities.security.User;
import ru.amalnev.jnms.common.services.SecurityService;
import ru.amalnev.jnms.web.constants.Constants;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/users")
public class UserController implements ApplicationContextAware
{
    @Setter
    private ApplicationContext applicationContext;

    @Setter(onMethod = @__({@Autowired}))
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
                        final @ModelAttribute User user,
                        final HttpServletRequest request) throws Exception
    {
        //TODO: clarify exception
        if (!request.getParameterMap().containsKey("action")) throw new Exception();
        final String requestedAction = request.getParameterMap().get("action")[0];
        if (requestedAction.equals("Save"))
        {
            final User existingUser = securityService.findUserById(user.getId()).orElse(new User());
            existingUser.setUsername(user.getUsername());
            if(user.getPassword() != null && !user.getPassword().isEmpty())
            {
                existingUser.setPassword(user.getPassword());
            }

            securityService.saveUser(existingUser);
        }

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
