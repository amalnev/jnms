package ru.amalnev.jnms.web.undo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import ru.amalnev.jnms.common.entities.AbstractEntity;

public abstract class AbstractUndoOperation implements UndoOperation, ApplicationContextAware
{
    @Getter
    @Setter
    private AbstractEntity entity;

    @Getter
    @Setter
    private ApplicationContext applicationContext;

    protected abstract String getOperationName();

    public String toString()
    {
        if(entity == null) return super.toString();

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Model object of type ");
        stringBuilder.append(entity.getClass().getSimpleName());
        stringBuilder.append(" with id = ");
        stringBuilder.append(entity.getId());
        stringBuilder.append(" has been ");
        stringBuilder.append(getOperationName());
        return stringBuilder.toString();
    }
}
