package ru.amalnev.jnms.web.flows;

import lombok.Getter;
import lombok.Setter;
import ru.amalnev.jnms.common.model.entities.ttms.Client;
import ru.amalnev.jnms.common.model.entities.ttms.ProblemDetails;
import ru.amalnev.jnms.common.model.entities.ttms.TroubleTicket;

import java.io.Serializable;

/**
 * Это DTO-объект, заполняемый данными по мере продвижения оператора
 * по формам WebFlow, реализующего создание нового трабл-тикета
 */
@Getter
@Setter
public class TroubleTicketRegisterModel implements Serializable
{
    private Client client;

    private ProblemDetails problemDetails;

    private TroubleTicket troubleTicket;
}
