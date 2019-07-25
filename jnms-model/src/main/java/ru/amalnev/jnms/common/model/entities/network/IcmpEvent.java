package ru.amalnev.jnms.common.model.entities.network;

import ru.amalnev.jnms.common.model.entities.NotifyOnChange;

import javax.persistence.Entity;

@Entity
@NotifyOnChange
public class IcmpEvent extends NetworkEvent
{
    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(isOutcome() ? " becomes reachable via ICMP" : " becomes unreachable via ICMP");
        return builder.toString();
    }
}
