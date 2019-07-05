package ru.amalnev.jnms.common.model.entities.ttms;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class ServiceQualityProblemDetails extends ProblemDetails
{
    private int uplink;

    private int downlink;

    private int losses;

    private int latency;
}
