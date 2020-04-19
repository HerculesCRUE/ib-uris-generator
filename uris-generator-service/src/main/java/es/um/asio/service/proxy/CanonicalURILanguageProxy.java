package es.um.asio.service.proxy;

import com.izertis.abstractions.service.DeleteService;
import com.izertis.abstractions.service.QueryService;
import com.izertis.abstractions.service.SaveService;
import es.um.asio.service.filter.CanonicalURILanguageFilter;
import es.um.asio.service.model.CanonicalURI;
import es.um.asio.service.model.CanonicalURILanguage;

import java.util.List;


/**
 * Proxy service for {@link CanonicalURILanguage}. Performs DTO conversion and permission checks.
 */
public interface CanonicalURILanguageProxy
        extends QueryService<CanonicalURILanguage, String, CanonicalURILanguageFilter>, SaveService<CanonicalURILanguage>, DeleteService<CanonicalURILanguage, String> {

    CanonicalURILanguage getAllByFullURI(String fullURI);

    List<CanonicalURILanguage> getAllByEntityNameAndPropertyName(String entityName, String propertyName);

    List<CanonicalURILanguage> getAllByEntityNameAndReference(String entityName, String reference);

    List<CanonicalURILanguage> getAllByEntityName(String entityName);

    List<CanonicalURILanguage> getAllByPropertyName(String propertyName);

    List<CanonicalURILanguage> getAllByElements(String domain, String subDomain, String language,String type, String concept, String reference);
}
