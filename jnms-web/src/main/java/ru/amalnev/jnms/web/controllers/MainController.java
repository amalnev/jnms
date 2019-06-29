package ru.amalnev.jnms.web.controllers;

import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.amalnev.jnms.web.constants.Constants;

/**
 * Контроллер, отвечающий за вывод начальной страницы приложения
 *
 * @author Aleksei Malnev
 */
@Controller
public class MainController implements ApplicationContextAware
{
    @Setter
    private ApplicationContext applicationContext;

    @GetMapping("/")
    private String show(final Model uiModel)
    {
        uiModel.addAttribute("viewType", "none");
        uiModel.addAttribute("springContext", applicationContext);

        return Constants.MAIN_VIEW;
    }
}
