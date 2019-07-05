package ru.amalnev.jnms.common.model.entities.ttms;

import lombok.Getter;
import lombok.Setter;
import ru.amalnev.jnms.common.model.entities.DisplayName;
import ru.amalnev.jnms.common.model.entities.MinPrivilege;
import ru.amalnev.jnms.common.model.repositories.EntityClass;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class NoServiceProblemDetails extends ProblemDetails
{
    private String layer1ConnectionStatus;

    private String layer2ConnectionStatus;

    private String layer3ConnectionStatus;
}
