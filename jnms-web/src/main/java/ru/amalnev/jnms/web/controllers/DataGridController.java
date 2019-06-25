package ru.amalnev.jnms.web.controllers;

import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.amalnev.jnms.common.entities.AbstractEntity;
import ru.amalnev.jnms.common.entities.DisplayName;
import ru.amalnev.jnms.common.utilities.ReflectionUtils;
import ru.amalnev.jnms.web.constants.Constants;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Этот контроллер отвечает за вывод таблицы имеющихся в системе
 * сущностей определенного класса
 *
 * @author Aleksei Malnev
 */
@Controller
@RequestMapping("/grid")
public class DataGridController implements ApplicationContextAware
{
    @Setter
    private ApplicationContext applicationContext;

    /**
     * Готовит данные для отображения HTML-таблицы, содержащей список сущностей заданного типа
     *
     * @param uiModel
     * @param entityClassName
     * @return
     * @throws ClassNotFoundException
     */
    @GetMapping
    protected String show(final Model uiModel,
                          final @RequestParam String entityClassName) throws ClassNotFoundException
    {
        //Находим класс сущности по имени
        final Class<? extends AbstractEntity> entityClass = (Class<? extends AbstractEntity>) Class.forName(entityClassName);

        //Находим соответствующий репозиторий
        final CrudRepository repository = ReflectionUtils.getRepositoryByEntityClass(applicationContext, entityClass);

        //Этот список будет содержать имена колонок в таблице
        final List<String> columnNames = new ArrayList<>();

        //Этот Map будет содержать значения ячеек в таблице. Ключ - id сущности, Значение - список значений полей сущности
        final Map<Long, List<String>> columnValues = new LinkedHashMap<>();

        //Получаем список отображаемых полей сущности, сортируем его в соответствии со значением
        //атрибута orderOfAppearance аннотации @DisplayName
        List<Field> displayFields = ReflectionUtils.getFields(entityClass).stream()
                .filter(field -> field.isAnnotationPresent(DisplayName.class))
                .sorted(Comparator.comparingInt(field -> field.getAnnotation(DisplayName.class).orderOfAppearance()))
                .collect(Collectors.toList());

        //Заполняем список имен колонок
        displayFields.forEach(field -> columnNames.add(field.getAnnotation(DisplayName.class).value()));
        uiModel.addAttribute("columnNames", columnNames);

        //Заполняем значения ячеек таблицы
        repository.findAll().forEach((final Object entity) -> {
            final List<String> values = new ArrayList<>();
            columnValues.put(((AbstractEntity) entity).getId(), values);
            displayFields.forEach(field -> {
                final Object fieldValue = ReflectionUtils.runGetter(field, entity);
                values.add(fieldValue == null ? "" : fieldValue.toString());
            });
        });

        //Передаем полученные данные на front
        uiModel.addAttribute("columnValues", columnValues);
        uiModel.addAttribute("entityClassName", entityClassName);
        uiModel.addAttribute("springContext", applicationContext);
        uiModel.addAttribute("viewType", "grid");
        return Constants.MAIN_VIEW;
    }

    /**
     * Удаляет сущность заданного класса по id
     *
     * @param uiModel
     * @param entityClassName
     * @param entityId
     * @return
     * @throws ClassNotFoundException
     */
    @GetMapping("/delete")
    private String delete(final Model uiModel,
                          final @RequestParam String entityClassName,
                          final @RequestParam Long entityId) throws ClassNotFoundException
    {
        //Находим класс сущности по имени
        final Class<? extends AbstractEntity> entityClass = (Class<? extends AbstractEntity>) Class.forName(entityClassName);

        //Находим соответствующий репозиторий
        final CrudRepository repository = ReflectionUtils.getRepositoryByEntityClass(applicationContext, entityClass);

        //Производим удаление
        repository.deleteById(entityId);

        //Возвращаемся к таблице сущностей
        return "redirect:/grid?entityClassName=" + entityClassName;
    }
}
