package ru.amalnev.jnms.web.undo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import ru.amalnev.jnms.common.entities.AbstractEntity;
import ru.amalnev.jnms.common.utilities.ReflectionUtils;

@Component
@Scope("prototype")
public class UndoUpdate extends AbstractUndoOperation
{
    @Getter
    @Setter
    private AbstractEntity originalEntity;

    @Override
    public void undo()
    {
        //Находим соответствующий репозиторий по классу сущности
        final CrudRepository repository = ReflectionUtils.getRepositoryByEntityClass(
                getApplicationContext(),
                getEntity().getClass());

        //Сохраняем в репозиторий состояние сущности до обновления
        repository.save(originalEntity);
    }

    @Override
    protected String getOperationName()
    {
        return "updated";
    }
}
