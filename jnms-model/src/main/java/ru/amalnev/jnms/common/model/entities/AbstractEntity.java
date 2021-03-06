package ru.amalnev.jnms.common.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Это общий базовый класс для всех сущностей предметной области
 *
 * @author Aleksei Malnev
 */
@MappedSuperclass
public abstract class AbstractEntity implements Cloneable, Serializable
{
    /**
     * Идентификатор сущности.
     * Поле в UI будет подписано "Object ID", редактирование будет запрещено. В форме редактирования
     * сущности и в таблице имеющихся сущностей будет выводиться на 1-м месте.
     */
    @Id
    @Getter
    @Setter
    @DisplayName(value = "Object ID", readonly = true, orderOfAppearance = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public AbstractEntity clone() throws CloneNotSupportedException
    {
        return (AbstractEntity) super.clone();
    }
}
