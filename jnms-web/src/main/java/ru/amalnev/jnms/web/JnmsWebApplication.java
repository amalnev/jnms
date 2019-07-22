package ru.amalnev.jnms.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@ComponentScan(basePackages = "ru.amalnev")
@PropertySources({
        @PropertySource("classpath:jnms.persistence.properties"),
        @PropertySource("classpath:jnms.web.properties")
})
public class JnmsWebApplication
{
    public static void main(String[] args) throws InterruptedException
    {
        SpringApplication.run(JnmsWebApplication.class, args);
    }
}
