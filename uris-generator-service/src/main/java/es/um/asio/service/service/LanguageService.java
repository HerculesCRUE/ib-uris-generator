package es.um.asio.service.service;

import com.izertis.abstractions.service.DeleteService;
import com.izertis.abstractions.service.QueryService;
import com.izertis.abstractions.service.SaveService;
import es.um.asio.service.filter.LanguageFilter;
import es.um.asio.service.model.Language;

import java.util.List;

public interface LanguageService
    extends QueryService<Language, String, LanguageFilter>, SaveService<Language>, DeleteService<Language, String> {

    List<Language> getAllByLanguage(final Language language);

    void setNotIsDefaultAllLanguages();

    List<Language> getDefaultLanguages();
}
