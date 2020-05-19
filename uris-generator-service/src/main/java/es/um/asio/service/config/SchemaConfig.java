package es.um.asio.service.config;

import com.google.common.collect.Sets;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import es.um.asio.service.config.properties.DatasourceProperties;
import es.um.asio.service.config.properties.JpaProperties;
import es.um.asio.service.config.properties.PersistenceProperties;
import es.um.asio.service.config.properties.URISChemaProperties;
import es.um.asio.service.model.User;
import es.um.asio.service.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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
