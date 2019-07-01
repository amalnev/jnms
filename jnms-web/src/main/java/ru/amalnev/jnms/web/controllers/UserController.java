package ru.amalnev.jnms.web.controllers;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.amalnev.jnms.common.entities.security.User;
import ru.amalnev.jnms.common.services.SecurityService;
import ru.amalnev.jnms.web.constants.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

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
                        final @RequestParam("pictureFile") MultipartFile pictureFile,
                        final HttpServletRequest request) throws Exception
    {
        //TODO: clarify exception
        if (!request.getParameterMap().containsKey("action")) throw new Exception();
        final String requestedAction = request.getParameterMap().get("action")[0];
        if (requestedAction.equals("Save"))
        {
            if(user.getId() == null) user.setId(-1L);
            final User existingUser = securityService.findUserById(user.getId()).orElse(new User());
            existingUser.setUsername(user.getUsername());
            if (user.getPassword() != null && !user.getPassword().isEmpty())
            {
                existingUser.setPassword(user.getPassword());
            }

            if(pictureFile != null && !pictureFile.isEmpty())
            {
                existingUser.setPicture(pictureFile.getBytes());
            }

            securityService.saveUser(existingUser);
        }

        return "redirect:/users";
    }

    @GetMapping("/image")
    private void getImage(final Model model,
                          HttpServletResponse response,
                          final @RequestParam Long userId) throws IOException
    {
        try
        {
            if(userId == null) throw new Exception();
            final User user = securityService.findUserById(userId).orElse(null);
            if(user == null || user.getPicture() == null || user.getPicture().length == 0) throw new Exception();

            response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
            response.getOutputStream().write(user.getPicture());
            response.getOutputStream().close();
        }
        catch(final Exception e)
        {
            //возвращаем картинку по умолчанию
            final Resource defaultImageResource = new ClassPathResource("static/assets/img/default-user.png");
            final InputStream inputStream = defaultImageResource.getInputStream();

            response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
            byte[] buffer = new byte[1024];
            while (true)
            {
                int bytesRead = inputStream.read(buffer);
                if (bytesRead == -1) break;
                response.getOutputStream().write(buffer, 0, bytesRead);
            }
            response.getOutputStream().close();
        }
    }

    @GetMapping("/delete")
    private String delete(final Model uiModel,
                          final @RequestParam Long userId)
    {
        securityService.deleteUserById(userId);
        return "redirect:/users";
    }
}
