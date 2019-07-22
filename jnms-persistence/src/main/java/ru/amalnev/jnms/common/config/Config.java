package ru.amalnev.jnms.common.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.amalnev.jnms.common.model.ModelAnalyzer;

/**
 * Конфигурационный бин common-модуля. Задает пути к пакетам JPA сущностей и
 * репозиториев. Также создает бин PasswordEncoder для хранения паролей в
 * зашифрованном виде.
 *
 * @author Aleksei Malnev
 */
@Configuration
@ComponentScan(basePackages = "ru.amalnev.jnms.common")
@EntityScan(basePackages = "ru.amalnev.jnms.common.model.entities")
@EnableJpaRepositories(basePackages = "ru.amalnev.jnms.common.model.repositories")
public class Config
{
    @Bean
    public PasswordEncoder makePasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelAnalyzer modelAnalyzer()
    {
        return new ModelAnalyzer();
    }
}
