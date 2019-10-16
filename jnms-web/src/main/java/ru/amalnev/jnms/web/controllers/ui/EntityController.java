package ru.amalnev.jnms.web.controllers.ui;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.amalnev.jnms.common.model.ModelAnalyzer;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;
import ru.amalnev.jnms.web.constants.Constants;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

/**
 * Контроллер, отвечающий за формирование страницы с формой редактирования
 * одной отдельной сущности заданного класса.
 *
 * @author Aleksei Malnev
 */
@Controller
@RequestMapping("/entity")
public class EntityController
{
    @Setter(onMethod = @__({@Autowired}))
    private ConversionService conversionService;

    @Setter(onMethod = @__({@Autowired}))
    private SmartValidator validator;

    @Setter(onMethod = @__({@Autowired}))
    private ModelAnalyzer modelAnalyzer;

    /**
     * Готовит данные для отображения формы редактирования
     * одной отдельной сущности заданного класса. Класс и ID сущности
     * приходят из запроса
     *
     * @param uiModel
     * @param entityClassName
     * @param entityId
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    @GetMapping
    private String show(final Model uiModel,
                        final @RequestParam String entityClassName,
                        final @RequestParam Long entityId)
            throws
            InstantiationException,
            IllegalAccessException,
            ClassNotFoundException
    {
        //Находим класс сущности по имени
        final Class<? extends AbstractEntity> entityClass = (Class<? extends AbstractEntity>) Class.forName(
                entityClassName);

        //Находим соответствующий репозиторий
        final CrudRepository repository = modelAnalyzer.getRepositoryByEntityClass(entityClass);

        //Находим сущность с запрошенным ID или создаем новую
        final AbstractEntity entity = entityId != null ?
                (AbstractEntity) repository.findById(entityId).orElse(entityClass.newInstance()) :
                entityClass.newInstance();

        //Передаем необходимые данные на front
        uiModel.addAttribute("viewType", "entity");
        uiModel.addAttribute("entity", entity);

        return Constants.MAIN_VIEW;
    }

    /**
     * Парсит и сохраняет в репозитории сущность, пришедшую в виде POST-запроса.
     *
     * @param uiModel
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/save")
    private String save(final Model uiModel,
                        final HttpServletRequest request) throws Exception
    {
        //Находим в данных POST-запроса имя класса сущности
        //TODO: clarify exception
        if (!request.getParameterMap().containsKey("entityClassName")) throw new Exception();
        final String entityClassName = request.getParameterMap().get("entityClassName")[0];

        //Находим в данных POST-запроса значение параметра "action". Этот параметр содержит информацию о том,
        //на какую кнопку нажал пользователь в форме - Save или Cancel.
        //TODO: clarify exception
        if (!request.getParameterMap().containsKey("action")) throw new Exception();
        final String requestedAction = request.getParameterMap().get("action")[0];
        if (requestedAction.equals("Cancel"))
        {
            //Пользователь нажал "Cancel", возвращаемся к таблице сущностей
            return "redirect:/grid?entityClassName=" + entityClassName;
        }

        //Пользователь не нажимал "Cancel", продолжаем работу

        //Находим класс сущности по имени
        final Class<? extends AbstractEntity> entityClass = (Class<? extends AbstractEntity>) Class.forName(
                entityClassName);

        //Находим соответствующий репозиторий
        final CrudRepository repository = modelAnalyzer.getRepositoryByEntityClass(entityClass);

        //Находим в данных POST-запроса параметр id и по нему находим соответствующую сущность
        //или создаем новую
        AbstractEntity entity = null;
        //UndoOperation undoOperation = null;
        if (request.getParameterMap().containsKey("id"))
        {
            final Long id = conversionService.convert(request.getParameterMap().get("id")[0], Long.class);
            if (id != null) entity = (AbstractEntity) repository.findById(id).orElse(null);
        }

        if (entity == null)
        {
            //Сущность с переданным в запросе id не была найдена в репозитории.
            //Создаем новый экземпляр сущности
            entity = entityClass.newInstance();

        }

        //Парсим данные из POST-запроса и записываем значения в соответствующие поля сущности.
        //Если с какими-то полями будут возникать проблемы, то ошибки будем записывать в этот объект Errors
        final Errors errors = new BindException(entity, entityClassName);
        for (final String fieldName : request.getParameterMap().keySet())
        {
            try
            {
                //Находим поле сущности по имени параметра из POST-запроса
                final Field entityField = modelAnalyzer.getDisplayableFields(entity.getClass()).stream()
                        .filter(field -> field.getName().equals(fieldName))
                        .findFirst().orElse(null);

                if (entityField == null) throw new NoSuchFieldException();

                //Берем новое значение поля из POST-запроса. Оно там содержится в виде строки.
                final String fieldValue = request.getParameterMap().get(fieldName)[0];

                //Если строка null или пустая - пропускаем это поле
                if (fieldValue == null || fieldValue.length() == 0) continue;

                if (modelAnalyzer.isManyToOneReference(entityField))
                {
                    //Если поле является ссылкой @ManyToOne, то строка fieldValue содержит ID сущности
                    //из другого репозитория, на которую идет ссылка.

                    //Конверитируем ID "чужой" сущности в Long
                    final Long referencedObjectId = conversionService.convert(fieldValue, Long.class);

                    //Находим "чужую" сущность в соответствующем репозитории по ID
                    final AbstractEntity referencedObject = (AbstractEntity) modelAnalyzer
                            .getRepositoryByEntityClass((Class<? extends AbstractEntity>) entityField.getType())
                            .findById(referencedObjectId)
                            .orElse(null);

                    //Записываем найденную "чужую" сущность в качестве значения поля
                    modelAnalyzer.setFieldValue(entityField, entity, referencedObject);
                }
                else
                {
                    //Поле является обычным полем данных, а не ссылкой на другую сущность

                    try
                    {
                        //Конвертируем значение поля из строки в соответствующий тип, устанавливаем новое значение
                        modelAnalyzer.setFieldValue(entityField, entity,
                                                    conversionService.convert(fieldValue, entityField.getType()));
                    }
                    catch (final Exception e)
                    {
                        //Конвертация не удалась. Добавляем ошибку и продолжаем.
                        errors.rejectValue(fieldName, "Must be convertible to " + entityField.getType());
                    }
                }
            }
            catch (NoSuchFieldException e)
            {
                continue;
            }
        }

        //В данный момент сущность заполнена данными, пришедшими из POST-запроса. Производим валидацию.
        validator.validate(entity, errors);
        if (errors.hasErrors())
        {
            //При валидации (или ранее - при конвертации) возникли ошибки. Передаем
            //объект ошибок в UI, и возвращаемся к странице формы без сохранения сущности в репозиторий
            uiModel.addAttribute("errors", errors);
            return show(uiModel, entityClassName, entity.getId());
        }

        //ошибок не было, сохраняем сущность в репозиторий
        repository.save(entity);

        //возвращаемся на страницу таблицы сущностей
        return "redirect:/grid?entityClassName=" + entityClassName;
    }
}
