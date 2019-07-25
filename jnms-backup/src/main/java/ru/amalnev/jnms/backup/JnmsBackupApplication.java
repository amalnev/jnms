package ru.amalnev.jnms.backup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.jpa.core.JpaExecutor;
import org.springframework.integration.jpa.inbound.JpaPollingChannelAdapter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.*;

@SpringBootApplication
@EntityScan(basePackageClasses = AbstractEntity.class)
public class JnmsBackupApplication
{
    public static void main(String[] args)
    {
        new SpringApplicationBuilder(JnmsBackupApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private BackupEntitySelector entitySelector;

    private Map<String, Long> backupMap = new HashMap<>();

    @Value("${backup.interval}")
    private Long backupInterval;

    @Value("${backup.root-directory}")
    private String backupDirectory;

    @Bean
    public MessageChannel jpaInputChannel()
    {
        return new DirectChannel();
    }

    @Bean
    public JpaExecutor jpaExecutor()
    {
        JpaExecutor jpaExecutor = new JpaExecutor(this.entityManagerFactory);
        jpaExecutor.setJpaQuery("from User");
        return jpaExecutor;
    }

    @Bean
    @InboundChannelAdapter(channel = "jpaInputChannel",
            poller = @Poller(fixedDelay = "1000"))
    public MessageSource<?> jpaInbound()
    {
        return new JpaPollingChannelAdapter(jpaExecutor());
    }

    @Bean
    @ServiceActivator(inputChannel = "jpaInputChannel")
    public MessageHandler handler()
    {
        return message ->
        {

            List<? extends AbstractEntity> entities = (List<? extends AbstractEntity>) message.getPayload();
            List<String> lines = new ArrayList<>();
            entities.forEach(entity ->
                             {
                                 backupMap.put(entity.getClass().getSimpleName(), System.currentTimeMillis());

                                 try
                                 {
                                     final ObjectMapper objectMapper = new ObjectMapper();
                                     lines.add(objectMapper.writeValueAsString(entity));
                                 }
                                 catch (JsonProcessingException e)
                                 {
                                     e.printStackTrace();
                                 }
                             });

            if (lines.size() > 0)
            {
                try
                {
                    Files.write(Paths.get(backupDirectory + "/" + entities.get(0).getClass().getSimpleName()),
                                lines,
                                UTF_8,
                                WRITE, CREATE, APPEND);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            //TODO: backup logic here
            //System.out.println(message.getPayload());


            final JpaPollingChannelAdapter jpaAdapter = (JpaPollingChannelAdapter) jpaInbound();

            try
            {
                final Field jpaExecutorField = jpaAdapter.getClass().getDeclaredField("jpaExecutor");
                jpaExecutorField.setAccessible(true);

                final JpaExecutor jpaExecutor = new JpaExecutor(entityManagerFactory);
                final String nextEntity = entitySelector.nextEntity();
                jpaExecutor.setJpaQuery("from " + nextEntity);
                jpaExecutorField.set(jpaAdapter, jpaExecutor);

                jpaExecutorField.setAccessible(false);

                final Long lastBackedUp = backupMap.get(nextEntity);
                if (lastBackedUp == null)
                {
                    backupMap.put(nextEntity, System.currentTimeMillis());
                }
                else
                {
                    final Long timeSinceLastBackup = System.currentTimeMillis() - lastBackedUp;
                    if (timeSinceLastBackup < backupInterval)
                    {
                        Thread.sleep(backupInterval - timeSinceLastBackup);
                    }
                }
            }
            catch (NoSuchFieldException | IllegalAccessException | InterruptedException e)
            {
                e.printStackTrace();
            }
        };
    }
}
