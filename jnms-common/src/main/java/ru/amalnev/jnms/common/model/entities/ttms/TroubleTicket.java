package ru.amalnev.jnms.common.model.entities.ttms;

import lombok.Getter;
import lombok.Setter;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;
import ru.amalnev.jnms.common.model.entities.DisplayName;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DisplayName(value = "Trouble tickets", orderOfAppearance = 8)
public class TroubleTicket extends AbstractEntity
{
    @Getter
    @Setter
    @ManyToOne
    @DisplayName(value = "Client", orderOfAppearance = 3)
    private Client client;

    @Getter
    @Setter
    @ManyToOne
    @DisplayName(value = "Details", orderOfAppearance = 4)
    private ProblemDetails problemDetails;

    @Getter
    @Setter
    @DisplayName(value = "Severity", orderOfAppearance = 5)
    private Integer severity;
}
