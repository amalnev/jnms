package ru.amalnev.jnmsweb.controllers;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import ru.amalnev.jnmscommon.entities.AbstractEntity;
import ru.amalnev.jnmscommon.entities.DisplayName;
import ru.amalnev.jnmscommon.utilities.ReflectionUtils;
import ru.amalnev.jnmsweb.constants.Constants;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/entity")
public class EntityController implements ApplicationContextAware
{
    @Setter
    private ApplicationContext applicationContext;

    @Setter(onMethod=@__({@Autowired}))
    private ConversionService conversionService;

    @Setter(onMethod=@__({@Autowired}))
    private SmartValidator validator;

    @GetMapping
    protected String show(final Model uiModel,
                          final @RequestParam String entityClassName,
                          final @RequestParam Long entityId)
            throws
            InstantiationException,
            IllegalAccessException,
            ClassNotFoundException
    {
        final Class<? extends AbstractEntity> entityClass = (Class<? extends AbstractEntity>) Class.forName(entityClassName);
        final CrudRepository repository = ReflectionUtils.getRepositoryByEntityClass(applicationContext, entityClass);

        final AbstractEntity entity = entityId != null ?
                (AbstractEntity) repository.findById(entityId).orElse(entityClass.newInstance()) :
                entityClass.newInstance();

//        final List<String> fieldNames = ReflectionUtils.getFields(entity.getClass()).stream()
//                .filter(field -> field.isAnnotationPresent(DisplayName.class))
//                .map(field -> field.getName())
//                .collect(Collectors.toList());

        uiModel.addAttribute("viewType", "entity");
        uiModel.addAttribute("entity", entity);
        uiModel.addAttribute("springContext", applicationContext);

        return Constants.MAIN_VIEW;
    }

    @PostMapping("/save")
    protected String save(final Model uiModel,
                          final HttpServletRequest request) throws Exception
    {
        //TODO: clarify exception
        if(!request.getParameterMap().containsKey("entityClassName")) throw new Exception();
        final String entityClassName = request.getParameterMap().get("entityClassName")[0];

        final Class<? extends AbstractEntity> entityClass = (Class<? extends AbstractEntity>) Class.forName(entityClassName);
        final CrudRepository repository = ReflectionUtils.getRepositoryByEntityClass(applicationContext, entityClass);

        AbstractEntity entity = null;
        if(request.getParameterMap().containsKey("id"))
        {
            final Long id = conversionService.convert(request.getParameterMap().get("id")[0], Long.class);
            if(id != null) entity = (AbstractEntity) repository.findById(id).orElse(null);
        }
        if(entity == null) entity = entityClass.newInstance();

        final Errors errors = new BindException(entity, entityClassName);
        for(final String fieldName : request.getParameterMap().keySet())
        {
            try
            {
                final Field entityField = ReflectionUtils.getFields(entity.getClass()).stream()
                        .filter(field -> field.getName().equals(fieldName))
                        .findFirst().orElse(null);
                if(entityField == null) throw new NoSuchFieldException();
                final String fieldValue = request.getParameterMap().get(fieldName)[0];
                if(fieldValue == null || fieldValue.length() == 0) continue;

                if(ReflectionUtils.isManyToOneReference(entityField))
                {
                    final Long referencedObjectId = conversionService.convert(fieldValue, Long.class);
                    final AbstractEntity referencedObject = (AbstractEntity) ReflectionUtils
                            .getRepositoryByEntityClass(applicationContext,
                                                        (Class<? extends AbstractEntity>) entityField.getType())
                            .findById(referencedObjectId)
                            .orElse(null);
                    ReflectionUtils.setFieldValue(entityField, entity, referencedObject);
                }
                else
                {
                    try
                    {
                        ReflectionUtils.setFieldValue(entityField, entity,
                                                      conversionService.convert(fieldValue, entityField.getType()));
                    }
                    catch (final Exception e)
                    {
                        errors.rejectValue(fieldName, "Must be convertible to " + entityField.getType());
                    }
                }
            }
            catch (NoSuchFieldException e)
            {
                continue;
            }
        }

        validator.validate(entity, errors);
        if(errors.hasErrors())
        {
            uiModel.addAttribute("errors", errors);
            return show(uiModel, entityClassName, entity.getId());
        }

        repository.save(entity);

        return "redirect:/grid?entityClassName=" + entityClassName;
    }
}
