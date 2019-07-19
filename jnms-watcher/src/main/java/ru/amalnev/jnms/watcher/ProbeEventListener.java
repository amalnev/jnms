package ru.amalnev.jnms.watcher;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.amalnev.jnms.common.model.entities.network.NetworkEvent;
import ru.amalnev.jnms.common.model.entities.network.Device;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
public class ProbeEventListener
{
    @Setter(onMethod = @__({@Autowired}))
    private RestTemplate restTemplate;

    @Value("${jnms.web.host}")
    private String jnmsWebHost;

    @Value("${jnms.web.port}")
    private Integer jnmsWebPort;

    @Value("${jnms.web.scheme}")
    private String jnmsWebScheme;

    @Async
    @EventListener
    public void handleProbeEvent(final ProbeEvent probeEvent) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException
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
            final StringBuilder uriBuilder = new StringBuilder();
            uriBuilder.append(jnmsWebScheme);
            uriBuilder.append("://");
            uriBuilder.append(jnmsWebHost);
            uriBuilder.append(":");
            uriBuilder.append(jnmsWebPort);
            uriBuilder.append("/jnms/api/entities/");
            uriBuilder.append(probeEvent.getNetworkEvent().getClass().getName());

            final String uri =  uriBuilder.toString();
            restTemplate.postForEntity(uri,
                                       probeEvent.getNetworkEvent(),
                                       probeEvent.getNetworkEvent().getClass());
        }
    }
}
