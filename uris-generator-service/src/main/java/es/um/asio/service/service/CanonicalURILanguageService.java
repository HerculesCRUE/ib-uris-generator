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
}
