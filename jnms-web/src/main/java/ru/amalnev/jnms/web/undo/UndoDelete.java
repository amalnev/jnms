package ru.amalnev.jnms.web.undo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import ru.amalnev.jnms.common.model.ModelAnalyzer;

@Component
@Scope("prototype")
public class UndoDelete extends AbstractUndoOperation
{
    @Autowired
    private ModelAnalyzer modelAnalyzer;

    @Autowired
    private CrudAspect aspect;

    @Override
    protected String getOperationName()
    {
        return "deleted";
    }

    @Override
    public void undo()
    {
        try
        {
            aspect.setEnabled(false);

            getEntity().setId(null);

            //Находим соответствующий репозиторий по классу сущности
            final CrudRepository repository = modelAnalyzer.getRepositoryByEntityClass(getEntity().getClass());

            //Создаем сущность в репозитории
            repository.save(getEntity());
        }
        finally
        {
            aspect.setEnabled(true);
        }
    }
}
