package ru.amalnev.jnms.web.controllers.ui;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.amalnev.jnms.common.model.ModelAnalyzer;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;
import ru.amalnev.jnms.common.model.entities.DisplayName;
import ru.amalnev.jnms.web.constants.Constants;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Этот контроллер отвечает за вывод таблицы имеющихся в системе
 * сущностей определенного класса
 *
 * @author Aleksei Malnev
 */
@Controller
@RequestMapping("/grid")
public class DataGridController
{
    @Setter(onMethod = @__({@Autowired}))
    private ModelAnalyzer modelAnalyzer;

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
        final Class<? extends AbstractEntity> entityClass = (Class<? extends AbstractEntity>) Class.forName(
                entityClassName);

        //Находим соответствующий репозиторий
        final CrudRepository repository = modelAnalyzer.getRepositoryByEntityClass(entityClass);

        //Этот список будет содержать имена колонок в таблице
        final List<String> columnNames = new ArrayList<>();

        //Этот Map будет содержать значения ячеек в таблице. Ключ - id сущности, Значение - список значений полей сущности
        final Map<Long, List<String>> columnValues = new LinkedHashMap<>();

        //Получаем список отображаемых полей сущности, сортируем его в соответствии со значением
        //атрибута orderOfAppearance аннотации @DisplayName
        List<Field> displayFields = modelAnalyzer.getDisplayableFields(entityClass);

        //Заполняем список имен колонок
        displayFields.forEach(field -> columnNames.add(field.getAnnotation(DisplayName.class).value()));
        uiModel.addAttribute("columnNames", columnNames);

        //Заполняем значения ячеек таблицы
        repository.findAll().forEach((final Object entity) ->
                                     {
                                         final List<String> values = new ArrayList<>();
                                         columnValues.put(((AbstractEntity) entity).getId(), values);
                                         displayFields.forEach(field ->
                                                               {
                                                                   final Object fieldValue = modelAnalyzer.runGetter(
                                                                           field, entity);
                                                                   values.add(
                                                                           fieldValue == null ? "" : fieldValue.toString());
                                                               });
                                     });

        //Передаем полученные данные на front
        uiModel.addAttribute("columnValues", columnValues);
        uiModel.addAttribute("entityClassName", entityClassName);
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
        final Class<? extends AbstractEntity> entityClass = (Class<? extends AbstractEntity>) Class.forName(
                entityClassName);

        //Находим соответствующий репозиторий
        final CrudRepository repository = modelAnalyzer.getRepositoryByEntityClass(entityClass);

        //Производим удаление
        repository.deleteById(entityId);

        //Возвращаемся к таблице сущностей
        return "redirect:/grid?entityClassName=" + entityClassName;
    }
}
