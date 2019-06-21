package ru.amalnev.jnmsweb.controllers;

import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.amalnev.jnmscommon.entities.AbstractEntity;
import ru.amalnev.jnmscommon.entities.DisplayName;
import ru.amalnev.jnmscommon.utilities.ReflectionUtils;
import ru.amalnev.jnmsweb.constants.Constants;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/grid")
public class DataGridController implements ApplicationContextAware
{
    @Setter
    private ApplicationContext applicationContext;

    @GetMapping
    protected String show(final Model uiModel,
                          final @RequestParam String entityClassName) throws ClassNotFoundException
    {
        final Class<? extends AbstractEntity> entityClass = (Class<? extends AbstractEntity>) Class.forName(entityClassName);
        final CrudRepository repository = ReflectionUtils.getRepositoryByEntityClass(applicationContext, entityClass);

        final List<String> columnNames = new ArrayList<>();
        final Map<Long, List<String>> columnValues = new LinkedHashMap<>();
        repository.findAll().forEach((final Object entity) -> {
            List<Field> displayFields = ReflectionUtils.getFields(entity.getClass()).stream()
                    .filter(field -> field.isAnnotationPresent(DisplayName.class))
                    .sorted(Comparator.comparingInt(field -> field.getAnnotation(DisplayName.class).orderOfAppearance()))
                    .collect(Collectors.toList());
            if(columnNames.isEmpty())
            {
                displayFields.forEach(field -> columnNames.add(field.getAnnotation(DisplayName.class).value()));
                uiModel.addAttribute("columnNames", columnNames);
            }

            final List<String> values = new ArrayList<>();
            columnValues.put(((AbstractEntity) entity).getId(), values);
            displayFields.forEach(field -> {
                final Object fieldValue = ReflectionUtils.runGetter(field, entity);
                values.add(fieldValue == null ? "" : fieldValue.toString());
            });
        });

        uiModel.addAttribute("columnValues", columnValues);
        uiModel.addAttribute("entityClassName", entityClassName);
        uiModel.addAttribute("springContext", applicationContext);
        uiModel.addAttribute("viewType", "grid");
        return Constants.MAIN_VIEW;
    }

    @GetMapping("/delete")
    private String delete(final Model uiModel,
                          final @RequestParam String entityClassName,
                          final @RequestParam Long entityId) throws ClassNotFoundException
    {
        final Class<? extends AbstractEntity> entityClass = (Class<? extends AbstractEntity>) Class.forName(entityClassName);
        final CrudRepository repository = ReflectionUtils.getRepositoryByEntityClass(applicationContext, entityClass);
        repository.deleteById(entityId);

        return "redirect:/grid?entityClassName=" + entityClassName;
    }
}
