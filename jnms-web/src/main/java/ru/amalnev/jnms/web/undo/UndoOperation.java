package ru.amalnev.jnms.web.undo;

import ru.amalnev.jnms.common.model.entities.AbstractEntity;

public interface UndoOperation
{
    AbstractEntity getEntity();

    void setEntity(final AbstractEntity entity);

    void undo();
}
