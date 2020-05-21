package es.um.asio.service.proxy;


import es.um.asio.audit.abstractions.service.DeleteService;
import es.um.asio.audit.abstractions.service.QueryService;
import es.um.asio.audit.abstractions.service.SaveService;
import es.um.asio.service.dto.UserDto;

//import com.izertis.abstractions.service.DeleteService;
//import com.izertis.abstractions.service.QueryService;
//import com.izertis.abstractions.service.SaveService;

import es.um.asio.service.filter.URIMapFilter;
import es.um.asio.service.model.URIMap;
import es.um.asio.service.model.User;

/**
 * Proxy service for {@link User}. Performs DTO conversion and permission checks.
 */
public interface URIMapProxy
        extends QueryService<URIMap, String, URIMapFilter>, SaveService<URIMap>, DeleteService<URIMap, String> {

}
