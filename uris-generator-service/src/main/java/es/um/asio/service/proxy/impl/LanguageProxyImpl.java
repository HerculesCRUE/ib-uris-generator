package es.um.asio.service.proxy.impl;


import es.um.asio.audit.abstractions.exception.NoSuchEntityException;
import es.um.asio.service.filter.CanonicalURIFilter;

// import com.izertis.abstractions.exception.NoSuchEntityException;

import es.um.asio.service.filter.LanguageFilter;
import es.um.asio.service.model.Language;
import es.um.asio.service.model.User;
import es.um.asio.service.proxy.LanguageProxy;
import es.um.asio.service.service.LanguageService;
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
public class LanguageProxyImpl implements LanguageProxy {

    /** The logger. */
    private final Logger logger = LoggerFactory.getLogger(LanguageProxyImpl.class);

    /**
     * Service layer.
     */
    @Autowired
    private LanguageService service;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Language> find(final String identifier) {
        return this.service.find(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Language> findPaginated(final LanguageFilter filter, final Pageable pageable) {
        return this.service.findPaginated(filter,pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Language> findAll() {
        return this.service.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Language save(final Language entity) {
        if (entity.getIsDefault()) {
            this.service.setNotIsDefaultAllLanguages();
        } else {
            if (this.service.getDefaultLanguages().isEmpty()) {
                entity.isDefault = true;
            }
        }
        List<Language> filtered = this.service.getAllByLanguage(entity);
        if (filtered.isEmpty()) {
            return this.service.save(entity);
        } else {
            for (Language e :filtered) {
                e.merge(entity);
                try {
                    return this.service.update(e);
                } catch (NoSuchEntityException ex) {
                    logger.error("NoSuchEntityException {}",ex);
                }
            }
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Language> save(final Iterable<Language> entities) {
        return this.service.save(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Language update(final Language entity) throws NoSuchEntityException {
        return this.service.update(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final Language entity) {
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
    public Language findOrCreate(String iso) {
        Language l = find(iso).orElse(null);
        if (l == null) {
            l = new Language();
            l.setIso(iso);
            save(l);
        }
        return l;
    }

    @Override
    public List<Language> getDefaultLanguages() {
        return this.service.getDefaultLanguages();
    }
}
