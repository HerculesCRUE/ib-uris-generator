package es.um.asio.service.service.impl;

import es.um.asio.service.config.properties.URISChemaProperties;
import es.um.asio.service.service.SchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchemaServiceImpl implements SchemaService {

    @Autowired
    private URISChemaProperties properties;

    @Override
    public String getCanonicalSchema() {
        return properties.getCanonicalURISchema();
    }

    @Override
    public String getCanonicalLanguageSchema() {
        return properties.getCanonicalURILanguageSchema();
    }
}
