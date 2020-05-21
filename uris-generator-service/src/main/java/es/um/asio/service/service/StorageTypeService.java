package es.um.asio.service.service;

import es.um.asio.audit.abstractions.service.DeleteService;
import es.um.asio.audit.abstractions.service.QueryService;
import es.um.asio.audit.abstractions.service.SaveService;
import es.um.asio.service.filter.StorageTypeFilter;
import es.um.asio.service.model.StorageType;

import java.util.List;

public interface StorageTypeService
    extends QueryService<StorageType, String, StorageTypeFilter>, SaveService<StorageType>, DeleteService<StorageType, String> {

    List<StorageType> getAllByStorageType(final StorageType storageType);

    StorageType getAllByName(final String name);

    void deleteByName(final String name);
}
