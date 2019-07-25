package ru.amalnev.jnms.watcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@ComponentScan(basePackages = "ru.amalnev")
@PropertySources({
        @PropertySource("classpath:jnms.persistence.properties"),
        @PropertySource("classpath:jnms.watcher.properties")
})
public class JnmsWatcherApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(JnmsWatcherApplication.class, args);
    }

}
