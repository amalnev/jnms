package ru.amalnev.jnms.watcher;

import org.springframework.context.event.EventListener;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.amalnev.jnms.common.model.entities.network.NetworkEvent;
import ru.amalnev.jnms.common.model.entities.network.Device;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
public class ProbeEventListener
{
    @Async
    @EventListener
    public void handleProbeEvent(final ProbeEvent probeEvent) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        final IProbe probe = (IProbe) probeEvent.getSource();
        final CrudRepository repository = probe.getEventRepository();
        final Class repositoryClass = repository.getClass();
        final Method findMostRecentEventMethod = repositoryClass.getMethod(
                "findFirstByDeviceOrderByTimestampDesc",
                Device.class);
        final NetworkEvent mostRecentEvent = (NetworkEvent) findMostRecentEventMethod.invoke(repository, probe.getDevice());
        if(mostRecentEvent == null || (mostRecentEvent.isOutcome() != probeEvent.getNetworkEvent().isOutcome()))
        {
            repository.save(probeEvent.getNetworkEvent());
        }
    }
}
