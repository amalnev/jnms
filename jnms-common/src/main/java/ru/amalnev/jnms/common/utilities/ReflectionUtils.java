package ru.amalnev.jnms.common.utilities;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.amalnev.jnms.common.entities.AbstractEntity;
import ru.amalnev.jnms.common.entities.DisplayName;
import ru.amalnev.jnms.common.entities.MinPrivilege;
import ru.amalnev.jnms.common.repositories.EntityClass;
import ru.amalnev.jnms.common.services.SecurityService;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectionUtils
{
    public static boolean isRepository(Class<?> aClass)
    {
        return aClass.isAnnotationPresent(Repository.class);
    }

    public static Class getEntityClass(final Object repositoryBean)
    {
        for (final Class<?> interfaceClass : AopProxyUtils.proxiedUserInterfaces(repositoryBean))
        {
            final EntityClass entityClassAnnotation = interfaceClass.getAnnotation(EntityClass.class);
            if (entityClassAnnotation != null) return entityClassAnnotation.value();
        }

        return null;
    }

    public static CrudRepository getRepositoryByEntityClass(final ApplicationContext springContext,
                                                            final Class<? extends AbstractEntity> entityClass)
    {
        for (final String beanName : springContext.getBeanDefinitionNames())
        {
            final Object beanInstance = springContext.getBean(beanName);
            if (beanInstance == null) continue;
            if (beanInstance instanceof CrudRepository &&
                    ReflectionUtils.getEntityClass(beanInstance).equals(entityClass))
            {
                return (CrudRepository) beanInstance;
            }
        }

        return null;
    }

    public static Map<Class<? extends AbstractEntity>, String> getDisplayableEntities(final ApplicationContext springContext)
    {
        List<Class<? extends AbstractEntity>> resultList = new ArrayList<>();
        Map<Class<? extends AbstractEntity>, String> result = new LinkedHashMap<>();
        for (final String beanName : springContext.getBeanDefinitionNames())
        {
            final Object beanInstance = springContext.getBean(beanName);
            if (beanInstance == null) continue;
            if (beanInstance instanceof CrudRepository)
            {
                final Class<? extends AbstractEntity> entityClass = getEntityClass(beanInstance);
                if (entityClass == null) continue;

                if (entityClass.isAnnotationPresent(DisplayName.class))
                {
                    final SecurityService securityService = (SecurityService) springContext.getBean(
                            SecurityService.class);
                    if (entityClass.isAnnotationPresent(MinPrivilege.class))
                    {
                        if (securityService.getCurrentPrivilegeLevel() >= entityClass.getAnnotation(
                                MinPrivilege.class).value())
                        {
                            resultList.add(entityClass);
                        }
                    }
                    else
                    {
                        resultList.add(entityClass);
                    }
                }
            }
        }

        Collections.sort(resultList,
                         Comparator.comparingInt(cls -> cls.getAnnotation(DisplayName.class).orderOfAppearance()));
        resultList.forEach(cls -> result.put(cls, cls.getAnnotation(DisplayName.class).value()));

        return result;
    }

    public static List<Field> getDisplayableFields(final Class<? extends AbstractEntity> entityClass)
    {
        return getFields(entityClass).stream()
                .filter(field -> isDisplayableField(field))
                .sorted(Comparator.comparingInt(field -> field.getAnnotation(DisplayName.class).orderOfAppearance()))
                .collect(Collectors.toList());
    }

    public static void setFieldValue(final Field field,
                                     final Object target,
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

    public static List<Field> getFields(Class<?> type)
    {
        final List<Field> fields = new ArrayList<Field>();
        return getFields(fields, type);
    }

    public static Object runGetter(Field field, Object o)
    {
        for (Method method : o.getClass().getMethods())
        {
            if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3)))
            {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase()))
                {
                    try
                    {
                        return method.invoke(o);
                    }
                    catch (IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
                    catch (InvocationTargetException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        return null;
    }

    public static boolean isDisplayableField(final Field field)
    {
        return field.isAnnotationPresent(DisplayName.class);
    }

    public static String getDisplayName(final Field field)
    {
        return field.getAnnotation(DisplayName.class).value();
    }

    public static boolean isOneToManyReference(final Field field)
    {
        return field.isAnnotationPresent(OneToMany.class);
    }

    public static boolean isManyToOneReference(final Field field)
    {
        return field.isAnnotationPresent(ManyToOne.class);
    }

    public static boolean isOrdinaryDataField(final Field field)
    {
        return !isManyToOneReference(field) && !isOneToManyReference(field) &&
                isDisplayableField(field);
    }

    public static boolean isDisabledField(final Field field)
    {
        if (isDisplayableField(field))
        {
            DisplayName displayNameAnnotation = field.getAnnotation(DisplayName.class);
            return displayNameAnnotation.readonly();
        }

        return false;
    }
}
