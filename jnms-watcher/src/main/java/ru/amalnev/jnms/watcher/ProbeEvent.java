package ru.amalnev.jnms.watcher;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import ru.amalnev.jnms.common.model.entities.network.NetworkEvent;

public class ProbeEvent extends ApplicationEvent
{
    @Getter
    @Setter
    private NetworkEvent networkEvent;

    public ProbeEvent(Object source)
    {
        super(source);
    }
}
