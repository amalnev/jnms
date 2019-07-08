package ru.amalnev.jnms.common.model.entities.ttms;

import lombok.Getter;
import lombok.Setter;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;
import ru.amalnev.jnms.common.model.entities.DisplayName;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

/**
 * Данный класс реализует DTO-объект, соответствующий форме ввода диагностической
 * информации. Оператор техподдержки заполняет эту форму в процессе удаленной диагностики
 * проблемы, результат попадает на сервер в виде объекта данного класса
 *
 * @author Aleksei Malnev
 */
@Getter
@Setter
@Entity
public class ProblemDetails extends AbstractEntity
{
    private String cpeDevice;

    private String serviceDescription;

    private String layer1ConnectionStatus;

    private String layer2ConnectionStatus;

    private String layer3ConnectionStatus;

    private String actionsPerformed;

    private Integer uplink;

    private Integer downlink;

    private Integer losses;

    private Integer latency;
}
