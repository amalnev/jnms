package ru.amalnev.jnms.common.model.reflection;

import lombok.Getter;
import org.springframework.data.repository.CrudRepository;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;
import ru.amalnev.jnms.common.model.entities.DisplayName;
import ru.amalnev.jnms.common.model.entities.MinPrivilege;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Getter
public class EntityReflection
{
    private Class<? extends AbstractEntity> entityClass;

    private String visibleName;

    private int orderOfAppearance = 0;

    private int minAllowedPrivilege = 0;

    private CrudRepository repository;

    private SortedSet<FieldReflection> fieldReflections = new TreeSet<>(Comparator.comparingInt(FieldReflection::getOrderOfAppearance));

    public EntityReflection(Class<? extends AbstractEntity> entityClass, CrudRepository repository)
    {
        this.entityClass = entityClass;
        this.repository = repository;
        orderOfAppearance = entityClass.getAnnotation(DisplayName.class).orderOfAppearance();
        visibleName = entityClass.getAnnotation(DisplayName.class).value();
        if (entityClass.isAnnotationPresent(MinPrivilege.class))
            minAllowedPrivilege = entityClass.getAnnotation(MinPrivilege.class).value();

        FieldReflection.getFields(entityClass).forEach(field -> {
            if (field.isAnnotationPresent(DisplayName.class))
            {
                fieldReflections.add(new FieldReflection(field));
            }
        });
    }

    public FieldReflection findFieldReflection(Field field)
    {
        for (final FieldReflection fieldReflection : fieldReflections)
        {
            if (fieldReflection.getField().equals(field))
            {
                return fieldReflection;
            }
        }

        return null;
    }

    public List<Field> getDisplayableFields()
    {
        return fieldReflections.stream()
                .map(FieldReflection::getField)
                .collect(Collectors.toList());
    }

}
