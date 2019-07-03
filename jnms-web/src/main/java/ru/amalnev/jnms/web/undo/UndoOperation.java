package ru.amalnev.jnms.web.undo;

import ru.amalnev.jnms.common.entities.AbstractEntity;

public interface UndoOperation
{
    AbstractEntity getEntity();

    void setEntity(final AbstractEntity entity);

    void undo();
}
