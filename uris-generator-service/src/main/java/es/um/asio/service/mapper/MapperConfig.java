package es.um.asio.service.mapper;

import es.um.asio.service.repository.RepositoryConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(excludeFilters = { @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "*..solr..*") })
@Import({RepositoryConfig.class})
public class MapperConfig {

}
