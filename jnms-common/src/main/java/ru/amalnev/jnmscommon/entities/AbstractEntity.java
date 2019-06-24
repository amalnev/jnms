package ru.amalnev.jnmscommon.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity
{
    @Id
    @Getter
    @Setter
    @DisplayName(value = "Object ID", readonly = true, orderOfAppearance = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
