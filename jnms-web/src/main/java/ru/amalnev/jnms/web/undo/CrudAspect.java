package ru.amalnev.jnms.web.undo;

import lombok.Setter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import ru.amalnev.jnms.common.model.ModelAnalyzer;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;

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

    @Pointcut("execution(* ru.amalnev.jnms.common.model.repositories.*.save(..))")
    public void saveMethodInsideACrudRepository()
    {
    }

    @Before("saveMethodInsideACrudRepository()")
    public void beforeCrudOperation(final JoinPoint joinPoint) throws CloneNotSupportedException
    {
        //Это сущность, которая собирается сохраниться в БД, нужно создать
        //подходящую операцию отмены сохранения
        final AbstractEntity newEntityState = (AbstractEntity) joinPoint.getArgs()[0];

        UndoOperation undoOperation;
        if (newEntityState.getId() == null || newEntityState.getId() == -1)
        {
            //Это новая сущность, будет сохраняться с помощью INSERT, создаем соответствующую операцию отмены
            undoOperation = applicationContext.getBean(UndoCreate.class);
            System.out.println("UNDO OP:" + undoOperation.hashCode());
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
                System.out.println("UNDO OP:" + undoOperation.hashCode());
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
