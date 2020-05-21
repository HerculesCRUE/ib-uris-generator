package es.um.asio.service.proxy;

import es.um.asio.audit.abstractions.service.DeleteService;
import es.um.asio.audit.abstractions.service.QueryService;
import es.um.asio.audit.abstractions.service.SaveService;
import es.um.asio.service.filter.StorageTypeFilter;
import es.um.asio.service.model.StorageType;


/**
 * Proxy service for {@link StorageType}. Performs DTO conversion and permission checks.
 */
public interface StorageTypeProxy
        extends QueryService<StorageType, String, StorageTypeFilter>, SaveService<StorageType>, DeleteService<StorageType, String> {

    StorageType findOrCreate(String storageTypeName);

    StorageType findByName(String storageTypeName);

    void deleteByName(final String name);
}
