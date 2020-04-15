package es.um.asio.service.proxy;

import com.izertis.abstractions.service.DeleteService;
import com.izertis.abstractions.service.QueryService;
import com.izertis.abstractions.service.SaveService;
import es.um.asio.service.filter.CanonicalURILanguageFilter;
import es.um.asio.service.model.CanonicalURILanguage;


/**
 * Proxy service for {@link CanonicalURILanguage}. Performs DTO conversion and permission checks.
 */
public interface CanonicalURILanguageProxy
        extends QueryService<CanonicalURILanguage, String, CanonicalURILanguageFilter>, SaveService<CanonicalURILanguage>, DeleteService<CanonicalURILanguage, String> {

}
