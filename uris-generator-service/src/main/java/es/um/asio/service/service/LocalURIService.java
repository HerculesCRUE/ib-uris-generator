package es.um.asio.service.service;

import es.um.asio.audit.abstractions.service.DeleteService;
import es.um.asio.audit.abstractions.service.QueryService;
import es.um.asio.audit.abstractions.service.SaveService;
import es.um.asio.service.filter.LocalURIFilter;
import es.um.asio.service.model.LocalURI;


import java.util.List;

public interface LocalURIService
    extends QueryService<LocalURI, String, LocalURIFilter>, SaveService<LocalURI>, DeleteService<LocalURI, String> {

    List<LocalURI> getAllByLocalURI(final LocalURI localURI);

    LocalURI getAllByLocalURIStr(final String localURI);

    LocalURI getAllByCanonicalURILanguageStrAndStorageTypeStr(final String canonicalURILanguage,final String storageTypeStr );
}
