package ru.amalnev.jnms.common.model.entities.network;

import lombok.Getter;
import lombok.Setter;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class NetworkEvent extends AbstractEntity
{
    private Long timestamp;

    @ManyToOne
    private Device device;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean outcome;

    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(timestamp);
        builder.append("] Device: ");
        builder.append(device.toString());
        builder.append(" (");
        builder.append(device.getOamIp());
        builder.append(" )");
        return builder.toString();
    }
}
