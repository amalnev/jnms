package ru.amalnev.jnms.common.model.entities.ttms;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;
import ru.amalnev.jnms.common.model.entities.DisplayName;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Данный класс представляет собой объект TroubleTicket - инцидент в системе
 * инцидент-менеджмента
 *
 * @author Aleksei Malnev
 */
@Entity
@DisplayName(value = "Trouble tickets", orderOfAppearance = 8)
public class TroubleTicket extends AbstractEntity
{
    /**
     * Клиент, для которого открыт данный тикет
     */
    @Getter
    @Setter
    @ManyToOne
    //@JsonManagedReference
    @DisplayName(value = "Client", orderOfAppearance = 3)
    private Client client;

    /**
     * Детализация проблемы. Заполняется оператором техподдержки в
     * ходе удаленной диагностики
     */
    @Getter
    @Setter
    @ManyToOne
    //@JsonManagedReference
    @DisplayName(value = "Details", orderOfAppearance = 4)
    private ProblemDetails problemDetails;

    /**
     * Приоритет данного тикета
     */
    @Getter
    @Setter
    @DisplayName(value = "Severity", orderOfAppearance = 5)
    private Integer severity;
}
