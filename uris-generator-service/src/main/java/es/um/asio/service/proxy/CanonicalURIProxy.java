package es.um.asio.service.proxy;

import com.izertis.abstractions.service.DeleteService;
import com.izertis.abstractions.service.QueryService;
import com.izertis.abstractions.service.SaveService;
import es.um.asio.service.filter.CanonicalURIFilter;
import es.um.asio.service.model.CanonicalURI;
import es.um.asio.service.model.User;

import java.util.List;

/**
 * Proxy service for {@link User}. Performs DTO conversion and permission checks.
 */
public interface CanonicalURIProxy
        extends QueryService<CanonicalURI, String, CanonicalURIFilter>, SaveService<CanonicalURI>, DeleteService<CanonicalURI, String> {

    List<CanonicalURI> getAllByFullURI(String fullURI);

    List<CanonicalURI> getAllByEntityNameAndPropertyName(String entityName, String propertyName);

    List<CanonicalURI> getAllByEntityNameAndReference(String entityName, String reference);

    List<CanonicalURI> getAllByEntityName(String entityName);

    List<CanonicalURI> getAllByEntityNameFromEntities(String entityName);

    List<CanonicalURI> getAllByPropertyName(String propertyName);

    List<CanonicalURI> getAllByPropertyFromProperties(String propertyName);

    List<CanonicalURI> getAllByElements(String domain, String subDomain, String type, String concept, String reference);

}
