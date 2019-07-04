package ru.amalnev.jnms.web.controllers.ui;

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
public class MainController
{

    @GetMapping("/")
    private String show(final Model uiModel)
    {
        uiModel.addAttribute("viewType", "none");

        return Constants.MAIN_VIEW;
    }
}
