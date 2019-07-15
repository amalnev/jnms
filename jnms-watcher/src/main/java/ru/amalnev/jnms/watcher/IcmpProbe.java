package ru.amalnev.jnms.watcher;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.amalnev.jnms.common.model.entities.network.Device;
import ru.amalnev.jnms.common.model.entities.network.IcmpEvent;
import ru.amalnev.jnms.common.model.repositories.IIcmpEventRepository;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IcmpProbe implements IProbe
{
    private static final Long DEFAULT_PERIOD = 1000L;

    @Getter
    @Setter
    private Device device;

    @Getter
    @Setter
    private Long periodMillis;

    @Setter(onMethod = @__({@Autowired}))
    private ApplicationEventPublisher probeEventPublisher;

    @Getter
    @Setter(onMethod = @__({@Autowired}))
    private IIcmpEventRepository eventRepository;

    private boolean lastState = true;

    public IcmpProbe()
    {
        periodMillis = DEFAULT_PERIOD;
    }

    @Override
    public void run()
    {
        final IcmpEvent icmpEvent = new IcmpEvent();
        final ProbeEvent probeEvent = new ProbeEvent(this);
        icmpEvent.setDevice(device);
        icmpEvent.setTimestamp(System.currentTimeMillis());

        if( Math.random() > 0.95)
            lastState = !lastState;

        icmpEvent.setOutcome(lastState);
        probeEvent.setNetworkEvent(icmpEvent);
        probeEventPublisher.publishEvent(probeEvent);
    }
}
