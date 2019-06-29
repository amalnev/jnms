package ru.amalnev.jnms.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@Controller
@SpringBootApplication(scanBasePackages = "ru.amalnev")
public class JnmsWebApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(JnmsWebApplication.class, args);
    }
}
