package es.um.asio.service.service;

import es.um.asio.audit.abstractions.service.DeleteService;
import es.um.asio.audit.abstractions.service.QueryService;
import es.um.asio.audit.abstractions.service.SaveService;
import es.um.asio.service.filter.CanonicalURIFilter;
import es.um.asio.service.model.CanonicalURI;

import java.util.List;

public interface CanonicalURIService
    extends QueryService<CanonicalURI, String, CanonicalURIFilter>, SaveService<CanonicalURI>, DeleteService<CanonicalURI, String> {

    List<CanonicalURI> getAllByCanonicalURI(final CanonicalURI canonicalURI);

    List<CanonicalURI> getAllByFullURI(final String fullURI);

    List<CanonicalURI> getAllByEntityNameAndPropertyName(final String entityName,final String propertyName);

    List<CanonicalURI> getAllByEntityNameAndReference(final String entityName,final String reference);

    List<CanonicalURI> getAllByEntityName(final String entityName);

    List<CanonicalURI> getAllByEntityNameAndIsEntity(final String entityName);

    List<CanonicalURI> getAllByPropertyName(final String propertyName);

    List<CanonicalURI> getAllByPropertyNameAndIsProperty(final String propertyName);

    List<CanonicalURI> getAllByElements(final String domain, final String subDomain, final String type, final String concept, final String reference);
}
