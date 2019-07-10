package ru.amalnev.jnms.common.model.entities.network;

import javax.persistence.Entity;

@Entity
public class IcmpEvent extends NetworkEvent
{
    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(" SOURCE: ICMP");
        return builder.toString();
    }
}
