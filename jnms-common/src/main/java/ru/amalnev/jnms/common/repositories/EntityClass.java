package ru.amalnev.jnms.common.repositories;

import ru.amalnev.jnms.common.entities.AbstractEntity;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EntityClass
{
    Class<? extends AbstractEntity> value();
}
