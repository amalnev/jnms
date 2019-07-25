package ru.amalnev.jnms.common.model;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.repository.CrudRepository;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;
import ru.amalnev.jnms.common.model.entities.DisplayName;
import ru.amalnev.jnms.common.model.entities.MinPrivilege;
import ru.amalnev.jnms.common.model.repositories.EntityClass;
import ru.amalnev.jnms.common.services.SecurityService;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * После полной инициализации контекста (когда все бины проинициализированы и готовы к работе),
 * контекст генерирует событие ContextRefreshedEvent. Здесь мы слушаем это событие и в момент
 * его появления анализируем какие репозитории есть в контексте, за какие сущности они отвечают,
 * какие их поля нужно вытаскивать в UI и т.д. Эта информация запоминается и впоследствии
 * используется для создания "обобщенного" CRUD-UI, способного работать со всеми найденными
 * сущностями.
 */
public class ModelAnalyzer implements ApplicationListener<ContextRefreshedEvent>
{
    @Autowired
    private ConfigurableListableBeanFactory beanFactory;

    @Autowired
    private SecurityService securityService;

    private Map<Class<? extends AbstractEntity>, CrudRepository> repositoryMap = new HashMap<>();

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent)
    {
        final ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        for (final String beanName : applicationContext.getBeanDefinitionNames())
        {
            final BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            final String beanScope = beanDefinition.getScope();

            if (beanScope == null || beanScope.equals("prototype") || beanScope.equals("session"))
                continue;

            final Object bean = applicationContext.getBean(beanName);
            if (bean instanceof CrudRepository)
            {
                for (final Class<?> interfaceClass : AopProxyUtils.proxiedUserInterfaces(bean))
                {
                    final EntityClass entityClassAnnotation = interfaceClass.getAnnotation(EntityClass.class);
                    if (entityClassAnnotation != null)
                        repositoryMap.put(entityClassAnnotation.value(), (CrudRepository) bean);
                }
            }
        }

    }

    /**
     * Возвращает Spring Data репозиторий, содержащий сущности заданного класса.
     *
     * @param entityClass Класс сущности, для которого нужно найти репозиторий
     * @return Бин репозитория, содержащий сущности заданного класса.
     */
    public CrudRepository getRepositoryByEntityClass(final Class<? extends AbstractEntity> entityClass)
    {
        return repositoryMap.get(entityClass);
    }

    /**
     * Собирает коллекцию классов сущностей, с которыми может работать текущий пользователь. В эту коллекцию
     * попадают сущности, аннотированные с помощью @DisplayName, для которых заданы минимальные права доступа
     * меньшие либо равные правам доступа текущего пользователя. Возвращается Map, в которой ключом является
     * класс сущности, значением - атрибут value() аннотации @DisplayName. Возвращаемый результат отсортирован в
     * соответствии с атрибутом orderOfAppearance аннотации @DisplayName.
     *
     * @return Коллекция классов сущностей, с которыми может работать текущий пользователь
     */
    public Map<Class<? extends AbstractEntity>, String> getDisplayableEntities()
    {
        List<Class<? extends AbstractEntity>> resultList = new ArrayList<>();
        Map<Class<? extends AbstractEntity>, String> result = new LinkedHashMap<>();
        for (final Class<? extends AbstractEntity> entityClass : repositoryMap.keySet())
        {
            if (entityClass.isAnnotationPresent(DisplayName.class))
            {
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
    public List<Field> getDisplayableFields(final Class<? extends AbstractEntity> entityClass)
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
    public void setFieldValue(final Field field,
                              final Object target,
                              final Object value) throws IllegalAccessException
    {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        field.set(target, value);
        field.setAccessible(accessible);
    }

    private List<Field> getFields(List<Field> fields, Class<?> type)
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
    public List<Field> getFields(Class<?> type)
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
    public Object runGetter(Field field, Object o)
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
                    catch (IllegalAccessException | InvocationTargetException e)
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
    public boolean isDisplayableField(final Field field)
    {
        return field.isAnnotationPresent(DisplayName.class);
    }

    /**
     * Возвращает атрибут value() аннотации @DisplayName для задданого поля.
     *
     * @param field
     * @return
     */
    public String getDisplayName(final Field field)
    {
        return field.getAnnotation(DisplayName.class).value();
    }

    /**
     * Возвращает true, если заданное поле помечено аннотацией @OneToMany
     *
     * @param field Поле, которое нужно проверить
     * @return true, если заданное поле помечено аннотацией @OneToMany
     */
    public boolean isOneToManyReference(final Field field)
    {
        return field.isAnnotationPresent(OneToMany.class);
    }

    /**
     * Возвращает true, если заданное поле помечено аннотацией @ManyToOne
     *
     * @param field Поле, которое нужно проверить
     * @return true, если заданное поле помечено аннотацией @ManyToOne
     */
    public boolean isManyToOneReference(final Field field)
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
    public boolean isOrdinaryDataField(final Field field)
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
    public boolean isDisabledField(final Field field)
    {
        if (isDisplayableField(field))
        {
            DisplayName displayNameAnnotation = field.getAnnotation(DisplayName.class);
            return displayNameAnnotation.readonly();
        }

        return false;
    }
}
