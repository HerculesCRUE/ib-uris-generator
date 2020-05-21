package es.um.asio.service.proxy;

import es.um.asio.audit.abstractions.service.DeleteService;
import es.um.asio.audit.abstractions.service.QueryService;
import es.um.asio.audit.abstractions.service.SaveService;
import es.um.asio.service.filter.LanguageTypeFilter;
import es.um.asio.service.model.LanguageType;

import java.util.List;

/**
 * Proxy service for {@link LanguageType}. Performs DTO conversion and permission checks.
 */
public interface LanguageTypeProxy
        extends QueryService<LanguageType, String, LanguageTypeFilter>, SaveService<LanguageType>, DeleteService<LanguageType, String> {

    public List<LanguageType> getByLanguageAndType(String l, String t);
}
