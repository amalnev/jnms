package ru.amalnev.jnms.web.controllers.rest;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.amalnev.jnms.common.model.ModelAnalyzer;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;

import java.util.List;

@RestController
@RequestMapping("/api/**")
public class EntityRestController
{
    @Setter(onMethod = @__({@Autowired}))
    private ModelAnalyzer modelAnalyzer;

    @GetMapping("/entities/{entityClassName}/{entityId}")
    private AbstractEntity getEntityById(final @PathVariable String entityClassName,
                                         final @PathVariable Long entityId) throws ClassNotFoundException
    {
        final Class entityClass = Class.forName(entityClassName);
        final CrudRepository repository = modelAnalyzer.getRepositoryByEntityClass(entityClass);
        if (repository == null || entityId == null) return null;
        return (AbstractEntity) repository.findById(entityId).orElse(null);
    }

    @GetMapping("/entities/{entityClassName}")
    private List<? extends AbstractEntity> getAllEntities(final @PathVariable String entityClassName) throws ClassNotFoundException
    {
        final Class entityClass = Class.forName(entityClassName);
        final CrudRepository repository = modelAnalyzer.getRepositoryByEntityClass(entityClass);
        if (repository == null) return null;
        return (List<? extends AbstractEntity>) repository.findAll();
    }

    @PostMapping(path = "/entities/{entityClassName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    private AbstractEntity addStudent(final @PathVariable String entityClassName,
                                      final @RequestBody AbstractEntity newEntity) throws ClassNotFoundException
    {
        final Class entityClass = Class.forName(entityClassName);
        final CrudRepository repository = modelAnalyzer.getRepositoryByEntityClass(entityClass);
        if (repository == null) return null;
        repository.save(newEntity);
        return newEntity;
    }

    @DeleteMapping("/entities/{entityClassName}/{entityId}")
    private void deleteStudent(final @PathVariable String entityClassName,
                               final @PathVariable Long entityId) throws ClassNotFoundException
    {
        final Class entityClass = Class.forName(entityClassName);
        final CrudRepository repository = modelAnalyzer.getRepositoryByEntityClass(entityClass);
        if (repository == null || entityId == null) return;
        repository.deleteById(entityId);
    }
}
