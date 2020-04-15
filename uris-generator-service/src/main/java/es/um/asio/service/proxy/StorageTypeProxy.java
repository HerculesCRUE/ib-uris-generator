package es.um.asio.service.proxy;

import com.izertis.abstractions.service.DeleteService;
import com.izertis.abstractions.service.QueryService;
import com.izertis.abstractions.service.SaveService;
import es.um.asio.service.filter.StorageTypeFilter;
import es.um.asio.service.model.StorageType;


/**
 * Proxy service for {@link StorageType}. Performs DTO conversion and permission checks.
 */
public interface StorageTypeProxy
        extends QueryService<StorageType, String, StorageTypeFilter>, SaveService<StorageType>, DeleteService<StorageType, String> {

}
