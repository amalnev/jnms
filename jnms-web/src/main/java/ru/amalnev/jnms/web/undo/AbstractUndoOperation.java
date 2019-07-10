package ru.amalnev.jnms.web.undo;

import lombok.Getter;
import lombok.Setter;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;

import java.io.Serializable;

public abstract class AbstractUndoOperation implements UndoOperation, Serializable
{
    @Getter
    @Setter
    private AbstractEntity entity;

    protected abstract String getOperationName();

    public String toString()
    {
        if (entity == null) return super.toString();

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
