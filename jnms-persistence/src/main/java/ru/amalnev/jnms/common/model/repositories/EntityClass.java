package ru.amalnev.jnms.common.model.repositories;

import ru.amalnev.jnms.common.model.entities.AbstractEntity;

import java.lang.annotation.*;

/**
 * Данная аннотация применяется к Spring Data репозиториям, для того, чтобы
 * во время выполнения с помощью рефлексии можно было определить за какую
 * сущность отвечает тот или иной репозиторий.
 *
 * @author Aleksei Malnev
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EntityClass
{
    /**
     * Класс сущностей, находящихся в репозитории, отмеченном
     * данной аннотацией.
     *
     * @return
     */
    Class<? extends AbstractEntity> value();
}
