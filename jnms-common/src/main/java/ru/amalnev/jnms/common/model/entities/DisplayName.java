package ru.amalnev.jnms.common.model.entities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация, управляющая отображением сущностей предметной области в UI.
 * Может указываться над сущностями и их полями. Сущности и поля,
 * не отмеченные данной аннотацией не попадут в UI автоматически (отмеченные - попадут).
 * Информация, содержащаяся в атрибутах данной аннотации будет учитываться при отображении UI.
 *
 * @author Aleksei Malnev
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface DisplayName
{
    /**
     * Human-readable имя данной сущности или поля.
     *
     * @return
     */
    String value();

    /**
     * Можно ли редактировать значение данного поля в UI. Не имеет смысла, если
     * аннотация написана над классом сущности.
     *
     * @return
     */
    boolean readonly() default false;

    /**
     * Порядок вывода в UI.
     * Для классов сущностей данный атрибут определяет порядок сортировки в меню сущностей.
     * Для полей данный атрибут определяет, в каком порядке будут формироваться формы редактирования
     * сущностей и таблицы сущностей в UI.
     *
     * @return
     */
    int orderOfAppearance() default 100;
}
