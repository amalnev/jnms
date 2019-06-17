package ru.amalnev.jnmscommon.repositories;

import ru.amalnev.jnmscommon.entities.AbstractEntity;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EntityClass
{
    Class<? extends AbstractEntity> value();
}
