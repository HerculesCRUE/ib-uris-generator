package es.um.asio.service.proxy;

import com.izertis.abstractions.service.DeleteService;
import com.izertis.abstractions.service.QueryService;
import com.izertis.abstractions.service.SaveService;
import es.um.asio.service.filter.LanguageTypeFilter;
import es.um.asio.service.model.LanguageType;
import es.um.asio.service.model.User;

/**
 * Proxy service for {@link LanguageType}. Performs DTO conversion and permission checks.
 */
public interface LanguageTypeProxy
        extends QueryService<LanguageType, String, LanguageTypeFilter>, SaveService<LanguageType>, DeleteService<LanguageType, String> {

}
