package es.um.asio.service.service;

import com.izertis.abstractions.service.DeleteService;
import com.izertis.abstractions.service.QueryService;
import com.izertis.abstractions.service.SaveService;
import es.um.asio.service.filter.LocalURIFilter;
import es.um.asio.service.model.LocalURI;


import java.util.List;

public interface LocalURIService
    extends QueryService<LocalURI, String, LocalURIFilter>, SaveService<LocalURI>, DeleteService<LocalURI, String> {

    List<LocalURI> getAllByLocalURI(final LocalURI localURI);

    List<LocalURI> getAllByLocalURIStr(final String localURI);

    List<LocalURI> getAllByCanonicalURILanguageStrAndStorageTypeStr(final String canonicalURILanguage,final String storageTypeStr );
}
