package ru.amalnev.jnms.web.undo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import ru.amalnev.jnms.common.model.ModelAnalyzer;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;

@Component
@Scope("prototype")
public class UndoUpdate extends AbstractUndoOperation
{
    @Getter
    @Setter
    private AbstractEntity originalEntity;

    @Autowired
    private ModelAnalyzer modelAnalyzer;

    @Override
    public void undo()
    {
        //Находим соответствующий репозиторий по классу сущности
        final CrudRepository repository = modelAnalyzer.getRepositoryByEntityClass(getEntity().getClass());

        //Сохраняем в репозиторий состояние сущности до обновления
        repository.save(originalEntity);
    }

    @Override
    protected String getOperationName()
    {
        return "updated";
    }
}
