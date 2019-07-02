package ru.amalnev.jnms.web.controllers;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.amalnev.jnms.web.undo.UndoOperationsStack;

/**
 * Контроллер, отвечающий за вывод списка откатываемых операций
 *
 * @author Aleksei Malnev
 */
@Controller
@RequestMapping("/undo")
public class UndoController implements ApplicationContextAware
{
    @Setter
    private ApplicationContext applicationContext;

    @Setter(onMethod = @__({@Autowired}))
    private UndoOperationsStack undoOperations;

    @GetMapping
    private String showOperations(final Model uiModel)
    {
        uiModel.addAttribute("viewType", "undo");
        uiModel.addAttribute("springContext", applicationContext);
        return "main-view";
    }

    @PostMapping
    private String undoLastOperation(final Model uiModel)
    {
        undoOperations.pop().undo();
        return showOperations(uiModel);
    }
}