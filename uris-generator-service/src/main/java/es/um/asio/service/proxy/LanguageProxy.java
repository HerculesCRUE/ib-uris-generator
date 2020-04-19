package es.um.asio.service.proxy;

import com.izertis.abstractions.service.DeleteService;
import com.izertis.abstractions.service.QueryService;
import com.izertis.abstractions.service.SaveService;
import es.um.asio.service.filter.LanguageFilter;
import es.um.asio.service.model.Language;

import java.util.Optional;

/**
 * Proxy service for {@link Language}. Performs DTO conversion and permission checks.
 */
public interface LanguageProxy
        extends QueryService<Language, String, LanguageFilter>, SaveService<Language>, DeleteService<Language, String> {

    Language findOrCreate(String ISO);
}
