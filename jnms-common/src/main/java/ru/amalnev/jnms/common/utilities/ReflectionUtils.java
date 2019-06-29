package ru.amalnev.jnms.common.utilities;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
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

/**
 * Класс-котейнер для статических утилит, в основном касающихся работы с
 * репозиториями и сущностями через рефлексию
 *
 * @author Aleksei Malnev
 */
public class ReflectionUtils
{
    /**
     * Находит класс сущности по бину репозитория.
     *
     * @param repositoryBean Spring Data репозиторий
     * @return Класс сущностей, содержащихся в данном репозитории
     */
    private static Class getEntityClass(final Object repositoryBean)
    {
        for (final Class<?> interfaceClass : AopProxyUtils.proxiedUserInterfaces(repositoryBean))
        {
            final EntityClass entityClassAnnotation = interfaceClass.getAnnotation(EntityClass.class);
            if (entityClassAnnotation != null) return entityClassAnnotation.value();
        }

        return null;
    }

    /**
     * Возвращает Spring Data репозиторий, содержащий сущности заданного класса.
     *
     * @param springContext Spring application context
     * @param entityClass   Класс сущности, для которого нужно найти репозиторий
     * @return Бин репозитория, содержащий сущности заданного класса.
     */
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

    /**
     * Собирает коллекцию классов сущностей, с которыми может работать текущий пользователь. В эту коллекцию
     * попадают сущности, аннотированные с помощью @DisplayName, для которых заданы минимальные права доступа
     * меньшие либо равные правам доступа текущего пользователя. Возвращается Map, в которой ключом является
     * класс сущности, значением - атрибут value() аннотации @DisplayName. Возвращаемый результат отсортирован в
     * соответствии с атрибутом orderOfAppearance аннотации @DisplayName.
     *
     * @param springContext Spring application context
     * @return Коллекция классов сущностей, с которыми может работать текущий пользователь
     */
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

    /**
     * Возвращает список отображаемых в UI полей заданного класса сущностей. В список попадают все
     * поля запрошенного класса и его родительских классов, помеченные аннотацией @DisplayName.
     * Возвращаемый список сортируется в порядке возрастания атрибута orderOfAppearance аннотации
     *
     * @param entityClass Класс сущности, для которой нужно собрать отображаемые поля
     * @return Список отображаемых полей для запрошенного класса.
     * @DisplayName
     */
    public static List<Field> getDisplayableFields(final Class<? extends AbstractEntity> entityClass)
    {
        return getFields(entityClass).stream()
                .filter(field -> isDisplayableField(field))
                .sorted(Comparator.comparingInt(field -> field.getAnnotation(DisplayName.class).orderOfAppearance()))
                .collect(Collectors.toList());
    }

    /**
     * Устанавливает заданное поле заданного объекта в заданное значение.
     * Может работать с полями, игнорируя уровни доступа private, protected.
     *
     * @param field  Поле, для которого нужно установить новое значение.
     * @param target Объект, для которого нужно установить значение поля.
     * @param value  Новое значение
     * @throws IllegalAccessException
     */
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

    /**
     * Возвращает значение заданного поля для заданного объекта.
     * Находит и вызывает getter для запрошенного поля.
     *
     * @param field Поле, для которого нужно получить текущее значение
     * @param o     Объект, для которого нужно получить значение поля
     * @return Значение поля.
     */
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

    /**
     * Возвращает true, если заданное поле помечено аннотацией @DisplayName
     *
     * @param field Поле, которое нужно проверить
     * @return true, если заданное поле помечено аннотацией @DisplayName
     */
    public static boolean isDisplayableField(final Field field)
    {
        return field.isAnnotationPresent(DisplayName.class);
    }

    /**
     * Возвращает атрибут value() аннотации @DisplayName для задданого поля.
     *
     * @param field
     * @return
     */
    public static String getDisplayName(final Field field)
    {
        return field.getAnnotation(DisplayName.class).value();
    }

    /**
     * Возвращает true, если заданное поле помечено аннотацией @OneToMany
     *
     * @param field Поле, которое нужно проверить
     * @return true, если заданное поле помечено аннотацией @OneToMany
     */
    public static boolean isOneToManyReference(final Field field)
    {
        return field.isAnnotationPresent(OneToMany.class);
    }

    /**
     * Возвращает true, если заданное поле помечено аннотацией @ManyToOne
     *
     * @param field Поле, которое нужно проверить
     * @return true, если заданное поле помечено аннотацией @ManyToOne
     */
    public static boolean isManyToOneReference(final Field field)
    {
        return field.isAnnotationPresent(ManyToOne.class);
    }

    /**
     * Возвращает true, если заданное поле не является ссылкой типа @OneToMany
     * или @ManyToOne и при этом помечено аннотацией @DisplayName
     *
     * @param field Поле, которое нужно проверить
     * @return
     */
    public static boolean isOrdinaryDataField(final Field field)
    {
        return !isManyToOneReference(field) && !isOneToManyReference(field) &&
                isDisplayableField(field);
    }

    /**
     * Возвращает true, если заданное поле помечено аннотацией @DisplayName
     * и ее атрибут readonly усановлен в true.
     *
     * @param field
     * @return
     */
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
