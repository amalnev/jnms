package ru.amalnev.jnms.backup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.amalnev.jnms.common.model.entities.Backup;

import javax.persistence.EntityManager;
import java.util.LinkedList;
import java.util.Queue;

@Component
public class BackupEntitySelector
{
    @Autowired
    private EntityManager entityManager;

    private Queue<String> entityNames = new LinkedList<>();

    @EventListener
    public void handleContextRefreshed(final ContextRefreshedEvent contextRefreshedEvent)
    {
        entityManager.getMetamodel().getEntities().forEach(entityType ->
                                                           {
                                                               try
                                                               {
                                                                   final Class entityClass = Class.forName(
                                                                           entityType.getJavaType().getCanonicalName());
                                                                   if (entityClass.isAnnotationPresent(Backup.class))
                                                                   {
                                                                       entityNames.add(entityType.getName());
                                                                   }
                                                               }
                                                               catch (ClassNotFoundException e)
                                                               {
                                                                   e.printStackTrace();
                                                               }
                                                           });

        if (!entityNames.contains("User")) entityNames.add("User");
    }

    public String nextEntity()
    {
        final String nextEntity = entityNames.poll();
        entityNames.add(nextEntity);
        return nextEntity;
    }
}
