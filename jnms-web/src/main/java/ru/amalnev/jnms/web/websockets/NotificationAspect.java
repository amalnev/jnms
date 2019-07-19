package ru.amalnev.jnms.web.websockets;

import lombok.Setter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ru.amalnev.jnms.common.model.entities.AbstractEntity;
import ru.amalnev.jnms.common.model.entities.NotifyOnChange;
import ru.amalnev.jnms.common.model.entities.network.NetworkEvent;

@Aspect
@Component
public class NotificationAspect
{
    @Setter(onMethod = @__({@Autowired}))
    private SimpMessagingTemplate template;

    @After("execution(* ru.amalnev.jnms.common.model.repositories.*.save(..))")
    private void afterCrudOperation(final JoinPoint joinPoint)
    {
        //Это сущность, которая собирается сохраниться в БД
        final AbstractEntity entity = (AbstractEntity) joinPoint.getArgs()[0];

        //Проверим, нужно ли для этой сущности отслеживать CRUD-операции
        //Если класс сущности не аннотирован как @NotifyOnChange, то считаем что не нужно
        if (!entity.getClass().isAnnotationPresent(NotifyOnChange.class)) return;

        //Если класс сущности не совместим с NetworkEvent, то тоже не нужно
        if (!NetworkEvent.class.isAssignableFrom(entity.getClass())) return;

        //Посылаем сообщение в веб-сокет
        template.convertAndSend("/topic/events",
                                new NotificationMessage(((NetworkEvent) entity).isOutcome(), entity.toString()));
    }
}
