package es.um.asio.service.service;

import es.um.asio.audit.abstractions.service.DeleteService;
import es.um.asio.audit.abstractions.service.QueryService;
import es.um.asio.audit.abstractions.service.SaveService;
import es.um.asio.service.filter.LanguageFilter;
import es.um.asio.service.model.Language;

import java.util.List;

public interface LanguageService
    extends QueryService<Language, String, LanguageFilter>, SaveService<Language>, DeleteService<Language, String> {

    List<Language> getAllByLanguage(final Language language);

    void setNotIsDefaultAllLanguages();

    List<Language> getDefaultLanguages();
}
