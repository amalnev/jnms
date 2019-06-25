package ru.amalnev.jnms.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManagerFactory;

/**
 * Данный класс отвечает за создание необходимых Spring JPA бинов, поскольку в данном
 * модуле boot не используется. Бины конфигурируются на основе информации из persistence.xml.
 * Кроме того, создается PasswordEncoder для хранения паролей в зашифрованном виде.
 *
 * @author Aleksei Malnev
 */
@Configuration
@ComponentScan
@EnableJpaRepositories(basePackages = "ru.amalnev.jnms.common.repositories")
public class Config
{
    @Bean
    public LocalEntityManagerFactoryBean entityManagerFactory()
    {
        LocalEntityManagerFactoryBean factoryBean = new LocalEntityManagerFactoryBean();
        factoryBean.setPersistenceUnitName("jnms-persistent-unit");

        return factoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory)
    {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);

        return transactionManager;
    }

    @Bean
    public PasswordEncoder makePasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
