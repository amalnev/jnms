package ru.amalnev.jnmsweb.controllers;

import lombok.Getter;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.repository.CrudRepository;
import org.springframework.ui.Model;
import ru.amalnev.jnmscommon.entities.AbstractEntity;
import ru.amalnev.jnmscommon.utilities.ReflectionUtils;

public abstract class AbstractDataController implements ApplicationContextAware
{
    @Getter
    private CrudRepository repository;

    private final Class<? extends AbstractEntity> entityClass;

    protected AbstractDataController(final Class<? extends AbstractEntity> entityClass)
    {
        this.entityClass = entityClass;
    }

    protected void init()
    {
        if(repository == null)
        {
            repository = ReflectionUtils.getRepositoryByEntityClass(getApplicationContext(), entityClass);
            if(repository == null)
                throw new BeanCreationException("No repository for entities represented by " + entityClass.getName());
        }
    }

    protected abstract String getPrefix();

    protected AbstractEntity newEntity() throws IllegalAccessException, InstantiationException
    {
        return entityClass.newInstance();
    }

    protected abstract ApplicationContext getApplicationContext();
}
