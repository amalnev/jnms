package ru.amalnev.jnms.web.undo;

import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import ru.amalnev.jnms.common.utilities.ReflectionUtils;

@Component
@Scope("prototype")
public class UndoCreate extends AbstractUndoOperation
{
    @Override
    public void undo()
    {
        //Находим соответствующий репозиторий по классу сущности
        final CrudRepository repository = ReflectionUtils.getRepositoryByEntityClass(
                getApplicationContext(),
                getEntity().getClass());

        //Удаляем сущность из репозитория
        repository.deleteById(getEntity().getId());
    }

    @Override
    protected String getOperationName()
    {
        return "created";
    }
}
