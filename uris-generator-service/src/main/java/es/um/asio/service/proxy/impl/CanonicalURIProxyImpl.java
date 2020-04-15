package es.um.asio.service.proxy.impl;

import com.izertis.abstractions.exception.NoSuchEntityException;
import es.um.asio.service.config.properties.URISChemaProperties;
import es.um.asio.service.filter.CanonicalURIFilter;
import es.um.asio.service.model.CanonicalURI;
import es.um.asio.service.model.User;
import es.um.asio.service.proxy.CanonicalURIProxy;
import es.um.asio.service.service.CanonicalURIService;
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



    /**
     * Service layer.
     */
    @Autowired
    private CanonicalURIService service;

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
        entity.generateFullURL("http://$domain$/$sub-domain$/$type$/$concept$/$reference$");
        List<CanonicalURI> filtered = this.service.getAllByCanonicalURI(entity);
        if (filtered.size() == 0) {
            return this.service.save(entity);
        } else {
            for (CanonicalURI e :filtered) {
                e.merge(entity);
                try {
                    return this.service.update(e);
                } catch (NoSuchEntityException ex) {
                    ex.printStackTrace();
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


}
