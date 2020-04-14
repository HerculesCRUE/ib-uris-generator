package es.um.asio.service.service;

import com.izertis.abstractions.service.DeleteService;
import com.izertis.abstractions.service.QueryService;
import com.izertis.abstractions.service.SaveService;
import es.um.asio.service.filter.URIMapFilter;
import es.um.asio.service.model.URIMap;

import java.util.List;

public interface URIMapService
    extends QueryService<URIMap, String, URIMapFilter>, SaveService<URIMap>, DeleteService<URIMap, String> {

    List<URIMap> getAllByURIMap(final URIMap uriMap);
}
