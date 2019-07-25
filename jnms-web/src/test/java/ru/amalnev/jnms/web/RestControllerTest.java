package ru.amalnev.jnms.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import ru.amalnev.jnms.common.model.ModelAnalyzer;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;
import ru.amalnev.jnms.common.model.entities.Backup;
import ru.amalnev.jnms.common.model.entities.DisplayName;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.Arrays;
import java.util.Map;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestControllerTest
{
    @LocalServerPort
    private int jnmsWebPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${jnms.web.host}")
    private String jnmsWebHost;

    @Value("${jnms.web.scheme}")
    private String jnmsWebScheme;

    @Value("${jnms.web.username}")
    private String jnmsWebUsername;

    @Value("${jnms.web.password}")
    private String jnmsWebPassword;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testEntityRestController() throws ClassNotFoundException
    {
        for(final EntityType<?> entityType : entityManager.getMetamodel().getEntities())
        {
            final Class entityClass = Class.forName(entityType.getJavaType().getCanonicalName());
            if (entityClass.isAnnotationPresent(DisplayName.class))
            {
                final StringBuilder uriBuilder = new StringBuilder();
                uriBuilder.append(jnmsWebScheme);
                uriBuilder.append("://");
                uriBuilder.append(jnmsWebHost);
                uriBuilder.append(":");
                uriBuilder.append(jnmsWebPort);
                uriBuilder.append("/jnms/api/entities/");
                uriBuilder.append(entityClass.getName());

                ResponseEntity<Object[]> responseEntity = restTemplate
                        .withBasicAuth(jnmsWebUsername, jnmsWebPassword)
                        .getForEntity(uriBuilder.toString(), Object[].class);

                for(Object entity : responseEntity.getBody())
                {
                    System.out.println(entity);
                }
            }
        }
    }
}
