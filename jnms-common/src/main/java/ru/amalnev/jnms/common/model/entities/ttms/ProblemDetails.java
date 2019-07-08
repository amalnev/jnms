package ru.amalnev.jnms.common.model.entities.ttms;

import lombok.Getter;
import lombok.Setter;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;
import ru.amalnev.jnms.common.model.entities.DisplayName;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

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
