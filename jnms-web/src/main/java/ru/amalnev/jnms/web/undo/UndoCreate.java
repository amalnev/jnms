package ru.amalnev.jnms.web.undo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import ru.amalnev.jnms.common.model.ModelAnalyzer;

@Component
@Scope("prototype")
public class UndoCreate extends AbstractUndoOperation
{
    @Autowired
    private ModelAnalyzer modelAnalyzer;

    @Override
    public void undo()
    {
        //Находим соответствующий репозиторий по классу сущности
        final CrudRepository repository = modelAnalyzer.getRepositoryByEntityClass(getEntity().getClass());

        //Удаляем сущность из репозитория
        repository.deleteById(getEntity().getId());
    }

    @Override
    protected String getOperationName()
    {
        return "created";
    }
}
