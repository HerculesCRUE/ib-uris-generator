package es.um.asio.service.service;

import com.izertis.abstractions.service.DeleteService;
import com.izertis.abstractions.service.QueryService;
import com.izertis.abstractions.service.SaveService;
import es.um.asio.service.filter.LanguageTypeFilter;
import es.um.asio.service.model.LanguageType;


import java.util.List;

public interface LanguageTypeService
    extends QueryService<LanguageType, String, LanguageTypeFilter>, SaveService<LanguageType>, DeleteService<LanguageType, String> {

    List<LanguageType> getAllByLanguageType(final LanguageType languageType);
    List<LanguageType> getByLanguageAndType(String l ,String t);
}
