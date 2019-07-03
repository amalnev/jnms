package ru.amalnev.jnms.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;
import ru.amalnev.jnms.web.undo.UndoOperationsStack;

@Configuration
public class BeanConfig
{
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public UndoOperationsStack undoOperationsStack()
    {
        return new UndoOperationsStack();
    }
}
