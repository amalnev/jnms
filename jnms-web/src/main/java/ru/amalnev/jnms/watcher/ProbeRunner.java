package ru.amalnev.jnms.watcher;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.amalnev.jnms.common.model.repositories.IDeviceRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

@Component
public class ProbeRunner
{
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);

    private Set<IProbe> probes = new HashSet<>();

    @Setter(onMethod = @__({@Autowired}))
    private ConfigurableListableBeanFactory beanFactory;

    @Setter(onMethod = @__({@Autowired}))
    private IDeviceRepository deviceRepository;

    @EventListener
    public void handleContextRefreshed(final ContextRefreshedEvent contextRefreshedEvent) throws ClassNotFoundException
    {
        final ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        for (final String beanName : applicationContext.getBeanDefinitionNames())
        {
            final BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            final String beanClassName = beanDefinition.getBeanClassName();
            if(beanClassName == null) continue;
            final Class beanClass = Class.forName(beanClassName);
            if(beanClass != null)
            {
                deviceRepository.findAll().forEach(device -> {
                    for(final Class iface : beanClass.getInterfaces())
                    {
                       if (iface.equals(IProbe.class))
                       {
                           final IProbe probe = (IProbe) applicationContext.getBean(beanClass);
                           probe.setDevice(device);
                           probes.add(probe);
                           executorService.scheduleAtFixedRate(probe::run,
                                                               0,
                                                               probe.getPeriodMillis(),
                                                               TimeUnit.MILLISECONDS);
                       }
                    }
                });
            }
        }
    }
}
