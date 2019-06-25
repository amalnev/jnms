package ru.amalnev.jnms.common.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

/**
 * Базовый класс для именованных сущностей предметной области.
 *
 * @author Aleksei Malnev
 */
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public abstract class NamedEntity extends AbstractEntity
{
    /**
     * Имя сущности.
     * Не должно быть null или пустым.
     * Должно быть уникальным в рамках конкретного типа сущностей.
     * Поле в UI подписано "Object name". В форме редактирования
     * сущности и в таблице имеющихся сущностей будет выводиться на 2-м месте.
     */
    @Getter
    @Setter
    @DisplayName(value = "Object name", orderOfAppearance = 2)
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Object names must be non-empty and unique across objects of the same type")
    private String name;

    /**
     * Метод toString() перегружен для корректного отображения именованных сущностей в UI.
     *
     * @return Строковое представление сущности.
     */
    @Override
    public String toString()
    {
        return name;
    }
}
