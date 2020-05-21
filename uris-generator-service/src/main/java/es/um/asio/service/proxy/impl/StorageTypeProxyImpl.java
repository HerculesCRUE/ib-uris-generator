package es.um.asio.service.proxy.impl;

import com.izertis.abstractions.exception.NoSuchEntityException;
import es.um.asio.service.filter.StorageTypeFilter;
import es.um.asio.service.model.StorageType;
import es.um.asio.service.model.User;
import es.um.asio.service.proxy.StorageTypeProxy;
import es.um.asio.service.service.StorageTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Proxy service implementation for {@link User}. Performs DTO conversion and permission checks.
 */
@Service
public class StorageTypeProxyImpl implements StorageTypeProxy {

    private final Logger logger = LoggerFactory.getLogger(StorageTypeProxyImpl.class);

    /**
     * Service layer.
     */
    @Autowired
    private StorageTypeService service;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<StorageType> find(final String identifier) {
        return this.service.find(identifier);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public Page<StorageType> findPaginated(final StorageTypeFilter filter, final Pageable pageable) {
        return this.service.findPaginated(filter,pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StorageType> findAll() {
        return this.service.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StorageType save(final StorageType entity) {
        List<StorageType> filtered = this.service.getAllByStorageType(entity);
        if (filtered.isEmpty()) {
            return this.service.save(entity);
        } else {
            for (StorageType e :filtered) {
                e.merge(entity);
                try {
                    return this.service.update(e);
                } catch (NoSuchEntityException ex) {
                    logger.error("NoSuchEntityException: {}",ex);
                }
            }
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StorageType> save(final Iterable<StorageType> entities) {
        return this.service.save(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StorageType update(final StorageType entity) throws NoSuchEntityException {
        return this.service.update(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final StorageType entity) {
        this.service.delete(entity);
    }

    @Override
    public void deleteByName(String name) {
        this.service.deleteByName(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final String identifier) {
        this.service.delete(identifier);
    }

    @Override
    public StorageType findOrCreate(String storageTypeName) {
        StorageType st = find(storageTypeName).orElse(null);
        if (st == null) {
            st = new StorageType(storageTypeName);
            save(st);
        }
        return st;
    }

    @Override
    public StorageType findByName(String storageTypeName) {
        return service.getAllByName(storageTypeName);
    }
}
