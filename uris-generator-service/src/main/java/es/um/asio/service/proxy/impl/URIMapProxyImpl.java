package es.um.asio.service.proxy.impl;

import es.um.asio.audit.abstractions.exception.NoSuchEntityException;
import es.um.asio.service.filter.URIMapFilter;
import es.um.asio.service.model.URIMap;
import es.um.asio.service.model.User;
import es.um.asio.service.proxy.URIMapProxy;
import es.um.asio.service.service.URIMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Optional;

/**
 * Proxy service implementation for {@link User}. Performs DTO conversion and permission checks.
 */
@Service
public class URIMapProxyImpl implements URIMapProxy {

    /**
     * Service layer.
     */
    @Autowired
    private URIMapService service;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<URIMap> find(final String identifier) {
        return this.service.find(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<URIMap> findPaginated(final URIMapFilter filter, final Pageable pageable) {
        return this.service.findPaginated(filter,pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<URIMap> findAll() {
        return this.service.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URIMap save(final URIMap entity) {
        List<URIMap> filtered = this.service.getAllByURIMap(entity);
        try {
            entity.completeEntity(filtered);
        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
        }
        if (filtered.size() == 0) {
            return this.service.save(entity);
        } else {
            for (URIMap uriMap :filtered) {
                uriMap.merge(entity);
                try {
                    return this.service.update(uriMap);
                } catch (NoSuchEntityException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<URIMap> save(final Iterable<URIMap> entities) {
        return this.service.save(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URIMap update(final URIMap entity) throws NoSuchEntityException {
        return this.service.update(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final URIMap entity) {
        this.service.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final String identifier) {
        this.service.delete(identifier);
    }


}
