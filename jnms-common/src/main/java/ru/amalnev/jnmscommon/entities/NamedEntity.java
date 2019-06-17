package ru.amalnev.jnmscommon.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public abstract class NamedEntity extends AbstractEntity
{
    @Getter
    @Setter
    @DisplayName(value = "Object name", orderOfAppearance = 2)
    @Column(unique = true, nullable = false)
    private String name;

    @Override
    public String toString()
    {
        return name;
    }
}
