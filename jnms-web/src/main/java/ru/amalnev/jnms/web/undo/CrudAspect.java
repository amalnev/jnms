package ru.amalnev.jnms.web.undo;

import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import ru.amalnev.jnms.common.model.ModelAnalyzer;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;
import ru.amalnev.jnms.common.model.entities.DisplayName;

@Aspect
@Component
public class CrudAspect implements ApplicationContextAware
{
    @Setter
    private ApplicationContext applicationContext;

    @Setter(onMethod = @__({@Autowired}))
    private ModelAnalyzer modelAnalyzer;

    @Setter(onMethod = @__({@Autowired}))
    private UndoOperationsStack undoOperations;

    @Getter
    @Setter
    private volatile boolean enabled = true;

    @Before("execution(* ru.amalnev.jnms.common.model.repositories.*.deleteById(..))")
    public void beforeCrudDeleteOperation(final JoinPoint joinPoint)
    {
        if (!enabled) return;

        //Это id сущности, которая собирается удалиться из БД, нужно создать
        //подходящую операцию отмены удаления
        final Long id = (Long) joinPoint.getArgs()[0];

        //Находим соответствующий репозиторий
        final CrudRepository repository = (CrudRepository) joinPoint.getTarget();

        if (repository == null) return;

        //Вытаскиваем из БД текущее состояние данной сущности (до DELETE)
        final AbstractEntity existingEntityState = (AbstractEntity) repository.findById(id).orElse(null);

        final UndoOperation undoOperation = applicationContext.getBean(UndoDelete.class);
        undoOperation.setEntity(existingEntityState);

        undoOperations.add(undoOperation);
    }

    @Before("execution(* ru.amalnev.jnms.common.model.repositories.*.save(..))")
    public void beforeCrudSaveOperation(final JoinPoint joinPoint)
    {
        if (!enabled) return;

        //Это сущность, которая собирается сохраниться в БД, нужно создать
        //подходящую операцию отмены сохранения
        final AbstractEntity newEntityState = (AbstractEntity) joinPoint.getArgs()[0];

        //Проверим, нужно ли для этой сущности отслеживать CRUD-операции
        //Если класс сущности не выводится в UI (не аннотирован как @DisplayName), то считаем что не нужно
        if (!newEntityState.getClass().isAnnotationPresent(DisplayName.class)) return;

        UndoOperation undoOperation;
        if (newEntityState.getId() == null || newEntityState.getId() == -1)
        {
            //Это новая сущность, будет сохраняться с помощью INSERT, создаем соответствующую операцию отмены
            undoOperation = applicationContext.getBean(UndoCreate.class);
        }
        else
        {
            //Это скорее всего существующая сущность, проверим это.

            //Находим соответствующий репозиторий
            final CrudRepository repository = modelAnalyzer.getRepositoryByEntityClass(newEntityState.getClass());

            //Вытаскиваем из БД текущее состояние данной сущности (до UPDATE)
            final AbstractEntity existingEntityState = (AbstractEntity) repository.findById(
                    newEntityState.getId()).orElse(null);
            if (existingEntityState == null)
            {
                //Это новая сущность, будет сохраняться с помощью INSERT, создаем соответствующую операцию отмены
                undoOperation = applicationContext.getBean(UndoCreate.class);
            }
            else
            {
                //TODO
                //Это существующая сущность, будет сохраняться с помощью UPDATE.
                //Поддержку отмены таких операций добавим позже
                return;
            }
        }

        //Передаем сущность в операцию отмены
        undoOperation.setEntity(newEntityState);

        //Добавляем операцию отмены в стек
        undoOperations.add(undoOperation);
    }
}
