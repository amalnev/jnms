package ru.amalnev.jnms.web.controllers.rest;

//import com.netflix.discovery.EurekaClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.amalnev.jnms.common.model.ModelAnalyzer;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/**")
public class EntityRestController
{
    @Setter(onMethod = @__({@Autowired}))
    private ModelAnalyzer modelAnalyzer;

/*    @Value("${spring.application.name}")
    private String appName;

    @Setter(onMethod = @__({@Autowired, @Lazy}))
    private EurekaClient eurekaClient;*/

    @GetMapping(path = "/entities/{entityClassName}/{entityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    private AbstractEntity getEntityById(final @PathVariable String entityClassName,
                                         final @PathVariable Long entityId) throws ClassNotFoundException
    {
        final Class entityClass = Class.forName(entityClassName);
        final CrudRepository repository = modelAnalyzer.getRepositoryByEntityClass(entityClass);
        if (repository == null || entityId == null) return null;
        return (AbstractEntity) repository.findById(entityId).orElse(null);
    }

    @GetMapping(path = "/entities/{entityClassName}", produces = MediaType.APPLICATION_JSON_VALUE)
    private List<? extends AbstractEntity> getAllEntities(final @PathVariable String entityClassName) throws ClassNotFoundException
    {
        final Class entityClass = Class.forName(entityClassName);
        final CrudRepository repository = modelAnalyzer.getRepositoryByEntityClass(entityClass);
        if (repository == null) return null;
        return (List<? extends AbstractEntity>) repository.findAll();
    }

    @PostMapping(path = "/entities/{entityClassName}", produces = MediaType.APPLICATION_JSON_VALUE)
    private AbstractEntity addEntity(final @PathVariable String entityClassName,
                                     final @RequestBody String requestBody) throws ClassNotFoundException, IOException
    {
        final Class entityClass = Class.forName(entityClassName);

        final ObjectMapper objectMapper = new ObjectMapper();
        final AbstractEntity newEntity = (AbstractEntity) objectMapper.readValue(requestBody, entityClass);

        final CrudRepository repository = modelAnalyzer.getRepositoryByEntityClass(entityClass);
        if (repository == null) return null;
        repository.save(newEntity);
        return newEntity;
    }

    @DeleteMapping("/entities/{entityClassName}/{entityId}")
    private void deleteEntity(final @PathVariable String entityClassName,
                              final @PathVariable Long entityId) throws ClassNotFoundException
    {
        final Class entityClass = Class.forName(entityClassName);
        final CrudRepository repository = modelAnalyzer.getRepositoryByEntityClass(entityClass);
        if (repository == null || entityId == null) return;
        repository.deleteById(entityId);
    }
}
