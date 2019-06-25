package ru.amalnev.jnms.common.entities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Данная аннотация регулирует доступ пользователей системы к сущностям предметной области
 * через UI. Определяет минимальный уровень привилегий, необходимый для того, чтобы иметь
 * право выполнять CRUD-операции с данным видом сущностей.
 *
 * @author Aleksei Malnev
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MinPrivilege
{
    int value();
}
