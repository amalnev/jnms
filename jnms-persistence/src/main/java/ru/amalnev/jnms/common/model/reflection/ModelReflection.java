package ru.amalnev.jnms.common.model.reflection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ModelReflection
{
    private SortedSet<EntityReflection> entityReflections = new TreeSet<>(Comparator.comparingInt(EntityReflection::getOrderOfAppearance));

    private FieldReflection findFieldReflection(Field field)
    {
        for (final EntityReflection entityReflection : entityReflections)
        {
            final FieldReflection result = entityReflection.findFieldReflection(field);
            if (result != null)
                return result;
        }

        return null;
    }

    private EntityReflection findEntityReflection(Class<? extends AbstractEntity> entityClass)
    {
        for (final EntityReflection entityReflection : entityReflections)
        {
            if (entityReflection.getEntityClass().equals(entityClass))
                return entityReflection;
        }

        return null;
    }

    public Map<Class<? extends AbstractEntity>, String> getDisplayableEntities(int privilegeLevel)
    {
        return entityReflections.stream()
                .filter(entityReflection -> entityReflection.getMinAllowedPrivilege() <= privilegeLevel)
                .collect(Collectors.toMap(EntityReflection::getEntityClass, EntityReflection::getVisibleName));
    }

    public CrudRepository getRepositoryByEntityClass(final Class<? extends AbstractEntity> entityClass)
    {
        return Objects.requireNonNull(findEntityReflection(entityClass)).getRepository();
    }

    public List<Field> getDisplayableFields(final Class<? extends AbstractEntity> entityClass)
    {
        return Objects.requireNonNull(findEntityReflection(entityClass)).getDisplayableFields();
    }

    public void addEntity(Class<? extends AbstractEntity> entityClass, CrudRepository repository)
    {
        entityReflections.add(new EntityReflection(entityClass, repository));
    }

    public boolean isDisabledField(final Field field)
    {
        return Objects.requireNonNull(findFieldReflection(field)).isDisabledField();
    }

    public boolean isOrdinaryDataField(final Field field)
    {
        return Objects.requireNonNull(findFieldReflection(field)).isOrdinaryDataField();
    }

    public boolean isManyToOneReference(final Field field)
    {
        return Objects.requireNonNull(findFieldReflection(field)).isManyToOneReference();
    }

    public boolean isOneToManyReference(final Field field)
    {
        return Objects.requireNonNull(findFieldReflection(field)).isOneToManyReference();
    }

    public String getDisplayName(final Field field)
    {
        return Objects.requireNonNull(findFieldReflection(field)).getDisplayName();
    }

    public boolean isDisplayableField(final Field field)
    {
        return Objects.requireNonNull(findFieldReflection(field)).isDisabledField();
    }

    public Object runGetter(Field field, Object o)
    {
        return Objects.requireNonNull(findFieldReflection(field)).runGetter(o);
    }

    public void setFieldValue(final Field field,
                              final Object target,
                              final Object value) throws IllegalAccessException
    {
        Objects.requireNonNull(findFieldReflection(field)).setFieldValue(target, value);
    }
}
