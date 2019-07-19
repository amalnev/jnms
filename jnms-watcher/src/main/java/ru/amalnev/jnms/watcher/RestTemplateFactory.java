package ru.amalnev.jnms.watcher;

import org.apache.http.HttpHost;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateFactory implements FactoryBean<RestTemplate>, InitializingBean
{
    @Value("${jnms.web.host}")
    private String jnmsWebHost;

    @Value("${jnms.web.port}")
    private Integer jnmsWebPort;

    @Value("${jnms.web.scheme}")
    private String jnmsWebScheme;

    @Value("${jnms.web.username}")
    private String jnmsWebUsername;

    @Value("${jnms.web.password}")
    private String jnmsWebPassword;

    private RestTemplate restTemplate;

    public RestTemplate getObject() {
        return restTemplate;
    }
    public Class<RestTemplate> getObjectType() {
        return RestTemplate.class;
    }
    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() {
        HttpHost host = new HttpHost(jnmsWebHost, jnmsWebPort, jnmsWebScheme);
        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactoryBasicAuth(host));
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(jnmsWebUsername, jnmsWebPassword));
    }
}
