package es.um.asio.service.proxy;

import com.izertis.abstractions.service.DeleteService;
import com.izertis.abstractions.service.QueryService;
import com.izertis.abstractions.service.SaveService;
import es.um.asio.service.filter.CanonicalURIFilter;
import es.um.asio.service.filter.URIMapFilter;
import es.um.asio.service.model.CanonicalURI;
import es.um.asio.service.model.URIMap;
import es.um.asio.service.model.User;

/**
 * Proxy service for {@link User}. Performs DTO conversion and permission checks.
 */
public interface CanonicalURIProxy
        extends QueryService<CanonicalURI, String, CanonicalURIFilter>, SaveService<CanonicalURI>, DeleteService<CanonicalURI, String> {

}
