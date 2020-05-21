package es.um.asio.service.proxy.impl;

import com.izertis.abstractions.exception.NoSuchEntityException;
import es.um.asio.service.filter.CanonicalURIFilter;
import es.um.asio.service.model.CanonicalURI;
import es.um.asio.service.model.User;
import es.um.asio.service.proxy.CanonicalURIProxy;
import es.um.asio.service.service.CanonicalURIService;
import es.um.asio.service.service.SchemaService;
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
public class CanonicalURIProxyImpl implements CanonicalURIProxy {

    /** The logger. */
    private final Logger logger = LoggerFactory.getLogger(CanonicalURIProxyImpl.class);

    /**
     * Service layer.
     */
    @Autowired
    private CanonicalURIService service;

    /**
     * Service layer.
     */
    @Autowired
    private SchemaService schemaService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<CanonicalURI> find(final String identifier) {
        return this.service.find(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CanonicalURI> findPaginated(final CanonicalURIFilter filter, final Pageable pageable) {
        return this.service.findPaginated(filter,pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CanonicalURI> findAll() {
        return this.service.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CanonicalURI save(final CanonicalURI entity) {
        entity.generateFullURL(schemaService.getCanonicalSchema());
        List<CanonicalURI> filtered = this.service.getAllByCanonicalURI(entity);
        if (filtered.isEmpty()) {
            return this.service.save(entity);
        } else {
            for (CanonicalURI e :filtered) {
                e.merge(entity);
                try {
                    return this.service.update(e);
                } catch (NoSuchEntityException ex) {
                    logger.error("NoSuchEntityException");
                }
            }
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CanonicalURI> save(final Iterable<CanonicalURI> entities) {
        return this.service.save(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CanonicalURI update(final CanonicalURI entity) throws NoSuchEntityException {
        return this.service.update(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final CanonicalURI entity) {
        this.service.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final String identifier) {
        this.service.delete(identifier);
    }

    @Override
    public List<CanonicalURI> getAllByFullURI(String fullURI) {
        return this.service.getAllByFullURI(fullURI);
    }

    @Override
    public List<CanonicalURI> getAllByEntityNameAndPropertyName(String entityName, String propertyName) {
        return this.service.getAllByEntityNameAndPropertyName(entityName,propertyName);
    }

    @Override
    public List<CanonicalURI> getAllByEntityNameAndReference(String entityName, String reference) {
        return this.service.getAllByEntityNameAndReference(entityName,reference);
    }

    @Override
    public List<CanonicalURI> getAllByEntityName(String entityName) {
        return this.service.getAllByEntityName(entityName);
    }

    @Override
    public List<CanonicalURI> getAllByEntityNameFromEntities(String entityName) {
        return this.service.getAllByEntityNameAndIsEntity(entityName);
    }

    @Override
    public List<CanonicalURI> getAllByPropertyName(String propertyName) {
        return this.service.getAllByPropertyName(propertyName);
    }

    @Override
    public List<CanonicalURI> getAllByPropertyFromProperties(String propertyName) {
        return this.service.getAllByPropertyNameAndIsProperty(propertyName);
    }

    @Override
    public List<CanonicalURI> getAllByElements(String domain, String subDomain, String type, String concept, String reference) {
        return this.service.getAllByElements(domain,subDomain,type,concept,reference);
    }
}
