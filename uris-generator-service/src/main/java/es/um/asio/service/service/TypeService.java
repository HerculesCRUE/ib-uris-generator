package es.um.asio.service.service;

import com.izertis.abstractions.service.DeleteService;
import com.izertis.abstractions.service.QueryService;
import com.izertis.abstractions.service.SaveService;
import es.um.asio.service.filter.TypeFilter;
import es.um.asio.service.model.Type;

import java.util.List;

public interface TypeService
    extends QueryService<Type, String, TypeFilter>, SaveService<Type>, DeleteService<Type, String> {

    List<Type> getAllByType(final Type type);
}
