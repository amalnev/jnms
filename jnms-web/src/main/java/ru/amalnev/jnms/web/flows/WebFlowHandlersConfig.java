package ru.amalnev.jnms.web.flows;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebFlowHandlersConfig
{
    @Bean
    public TroubleTicketRegisterHandler troubleTicketRegisterHandler()
    {
        return new TroubleTicketRegisterHandler();
    }
}
