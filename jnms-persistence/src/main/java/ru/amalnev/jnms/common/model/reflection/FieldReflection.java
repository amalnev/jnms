package ru.amalnev.jnms.common.model.reflection;

import lombok.Getter;
import ru.amalnev.jnms.common.model.entities.DisplayName;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class FieldReflection
{
    private Field field;

    private String visibleName;

    private int orderOfAppearance;

    public FieldReflection(Field field)
    {
        this.field = field;
        orderOfAppearance = field.getAnnotation(DisplayName.class).orderOfAppearance();
        visibleName = field.getAnnotation(DisplayName.class).value();
    }

    public boolean isDisabledField()
    {
        if (isDisplayableField())
        {
            final DisplayName displayNameAnnotation = field.getAnnotation(DisplayName.class);
            return displayNameAnnotation.readonly();
        }

        return false;
    }

    public boolean isOrdinaryDataField()
    {
        return !isManyToOneReference() && !isOneToManyReference() &&
               isDisplayableField();
    }

    public boolean isManyToOneReference()
    {
        return field.isAnnotationPresent(ManyToOne.class);
    }

    public boolean isOneToManyReference()
    {
        return field.isAnnotationPresent(OneToMany.class);
    }

    public String getDisplayName()
    {
        return visibleName;
    }

    public boolean isDisplayableField()
    {
        return true;
    }

    public Object runGetter(Object target)
    {
        for (Method method : target.getClass().getMethods())
        {
            if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3)))
            {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase()))
                {
                    try
                    {
                        return method.invoke(target);
                    }
                    catch (IllegalAccessException | InvocationTargetException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        return null;
    }

    public void setFieldValue(final Object target,
                              final Object value) throws IllegalAccessException
    {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        field.set(target, value);
        field.setAccessible(accessible);
    }

    private static List<Field> getFields(List<Field> fields, Class<?> type)
    {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null)
        {
            getFields(fields, type.getSuperclass());
        }

        return fields;
    }

    /**
     * Рекурсивно собирает все поля, объявленные в заданном классе и его предках.
     *
     * @param type Класс, для которого нужно собрать поля.
     * @return Список полей запрошенного класса и его предков.
     */
    public static List<Field> getFields(Class<?> type)
    {
        final List<Field> fields = new ArrayList<>();
        return getFields(fields, type);
    }
}
