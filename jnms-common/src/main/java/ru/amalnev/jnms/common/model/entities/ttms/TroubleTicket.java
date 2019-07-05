package ru.amalnev.jnms.common.model.entities.ttms;

import lombok.Getter;
import lombok.Setter;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class TroubleTicket extends AbstractEntity
{
    @ManyToOne
    private Client client;

    @ManyToOne
    private NoServiceProblemDetails noServiceProblemDetails;

    @ManyToOne
    private ServiceQualityProblemDetails serviceQualityProblemDetails;

    private int severity;

    @ManyToOne
    private WorkGroup assignedGroup;
}
