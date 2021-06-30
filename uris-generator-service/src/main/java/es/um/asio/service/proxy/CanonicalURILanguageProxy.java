package es.um.asio.service.proxy;

import es.um.asio.audit.abstractions.service.DeleteService;
import es.um.asio.audit.abstractions.service.QueryService;
import es.um.asio.audit.abstractions.service.SaveService;
import es.um.asio.service.filter.CanonicalURILanguageFilter;
import es.um.asio.service.model.CanonicalURILanguage;
import es.um.asio.service.model.Type;

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

    List<CanonicalURILanguage> getAllByEntityNameFromEntities(String entityName);

    List<CanonicalURILanguage> getAllByPropertyName(String propertyName);

    List<CanonicalURILanguage> getAllByPropertyNameFromProperties(String propertyName);

    List<CanonicalURILanguage> getAllByElements(String domain, String subDomain, String language,String type, String concept, String reference);
}
