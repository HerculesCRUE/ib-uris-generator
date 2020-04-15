package es.um.asio.service.proxy.impl;

import com.izertis.abstractions.exception.NoSuchEntityException;
import es.um.asio.service.filter.CanonicalURILanguageFilter;
import es.um.asio.service.model.CanonicalURILanguage;
import es.um.asio.service.model.User;
import es.um.asio.service.proxy.CanonicalURILanguageProxy;
import es.um.asio.service.service.CanonicalURILanguageService;
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
public class CanonicalURILanguageProxyImpl implements CanonicalURILanguageProxy {


    /**
     * Service layer.
     */
    @Autowired
    private CanonicalURILanguageService service;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<CanonicalURILanguage> find(final String identifier) {
        return this.service.find(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CanonicalURILanguage> findPaginated(final CanonicalURILanguageFilter filter, final Pageable pageable) {
        return this.service.findPaginated(filter,pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CanonicalURILanguage> findAll() {
        return this.service.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CanonicalURILanguage save(final CanonicalURILanguage entity) {
        entity.generateFullURL("http://$domain$/$sub-domain$/$language$/$type$/$concept$/$reference$");
        List<CanonicalURILanguage> filtered = this.service.getAllByCanonicalURILanguage(entity);
        if (filtered.size() == 0) {
            return this.service.save(entity);
        } else {
            for (CanonicalURILanguage e :filtered) {
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
    public List<CanonicalURILanguage> save(final Iterable<CanonicalURILanguage> entities) {
        return this.service.save(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CanonicalURILanguage update(final CanonicalURILanguage entity) throws NoSuchEntityException {
        return this.service.update(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final CanonicalURILanguage entity) {
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
