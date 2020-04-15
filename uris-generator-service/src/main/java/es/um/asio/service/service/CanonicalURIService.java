package es.um.asio.service.service;

import com.izertis.abstractions.service.DeleteService;
import com.izertis.abstractions.service.QueryService;
import com.izertis.abstractions.service.SaveService;
import es.um.asio.service.filter.CanonicalURIFilter;
import es.um.asio.service.model.CanonicalURI;


import java.util.List;

public interface CanonicalURIService
    extends QueryService<CanonicalURI, String, CanonicalURIFilter>, SaveService<CanonicalURI>, DeleteService<CanonicalURI, String> {

    List<CanonicalURI> getAllByCanonicalURI(final CanonicalURI canonicalURI);
}
