package ru.amalnev.jnms.common.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public abstract class NamedEntity extends AbstractEntity
{
    @Getter
    @Setter
    @DisplayName(value = "Object name", orderOfAppearance = 2)
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Object names must be non-empty and unique across objects of the same type")
    private String name;

    @Override
    public String toString()
    {
        return name;
    }
}
