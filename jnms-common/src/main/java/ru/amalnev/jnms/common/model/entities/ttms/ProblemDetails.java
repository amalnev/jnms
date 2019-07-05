package ru.amalnev.jnms.common.model.entities.ttms;

import lombok.Getter;
import lombok.Setter;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;
import ru.amalnev.jnms.common.model.entities.DisplayName;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class ProblemDetails extends AbstractEntity
{
    @Getter
    @Setter
    private String cpeDevice;

    @Getter
    @Setter
    private String serviceDescription;

    @Getter
    @Setter
    private String actionsPerformed;
}
