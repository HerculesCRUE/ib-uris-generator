package es.um.asio.service.service;

import com.izertis.abstractions.service.DeleteService;
import com.izertis.abstractions.service.QueryService;
import com.izertis.abstractions.service.SaveService;
import es.um.asio.service.filter.CanonicalURILanguageFilter;
import es.um.asio.service.model.CanonicalURILanguage;

import java.util.List;

public interface CanonicalURILanguageService
    extends QueryService<CanonicalURILanguage, String, CanonicalURILanguageFilter>, SaveService<CanonicalURILanguage>, DeleteService<CanonicalURILanguage, String> {

    List<CanonicalURILanguage> getAllByCanonicalURILanguage(final CanonicalURILanguage canonicalURILanguage);

    CanonicalURILanguage getAllByFullURI(final String fullURI);

    List<CanonicalURILanguage> getAllByEntityNameAndPropertyName(final String entityName,final String propertyName);

    List<CanonicalURILanguage> getAllByEntityNameAndReference(final String entityName,final String reference);

    List<CanonicalURILanguage> getAllByEntityName(final String entityName);

    List<CanonicalURILanguage> getAllByPropertyName(final String propertyName);

    List<CanonicalURILanguage> getAllByElements(final String domain, final String subDomain, final String language, final String type, final String concept, final String reference);
}
