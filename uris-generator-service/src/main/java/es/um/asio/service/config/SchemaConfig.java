package es.um.asio.service.config;

import es.um.asio.service.config.properties.URISChemaProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Persistence configuration.
 */
@Configuration
@EnableConfigurationProperties(URISChemaProperties.class)
public class SchemaConfig {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Configuration properties.
     */
    @Autowired
    private URISChemaProperties properties;


    @Bean
    public String canonicalURISchema() {
        return this.properties.getCanonicalURISchema();
    }

    @Bean
    public String canonicalLanguageURISchema() {
        return this.properties.getCanonicalURILanguageSchema();
    }
}
