package es.um.asio.service.proxy.impl;

import com.izertis.abstractions.exception.NoSuchEntityException;
import es.um.asio.service.filter.LanguageFilter;
import es.um.asio.service.filter.LanguageTypeFilter;
import es.um.asio.service.model.Language;
import es.um.asio.service.model.LanguageType;
import es.um.asio.service.model.User;
import es.um.asio.service.proxy.LanguageProxy;
import es.um.asio.service.proxy.LanguageTypeProxy;
import es.um.asio.service.service.LanguageService;
import es.um.asio.service.service.LanguageTypeService;
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
public class LanguageTypeProxyImpl implements LanguageTypeProxy {



    /**
     * Service layer.
     */
    @Autowired
    private LanguageTypeService service;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<LanguageType> find(final String identifier) {
        return this.service.find(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<LanguageType> findPaginated(final LanguageTypeFilter filter, final Pageable pageable) {
        return this.service.findPaginated(filter,pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LanguageType> findAll() {
        return this.service.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LanguageType save(final LanguageType entity) {
        List<LanguageType> filtered = this.service.getByLanguageAndType(entity.getLanguage().getISO(),entity.getType().getCode());
        if (filtered.size() == 0) {
            return this.service.save(entity);
        } else {
            for (LanguageType e :filtered) {
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
    public List<LanguageType> save(final Iterable<LanguageType> entities) {
        return this.service.save(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LanguageType update(final LanguageType entity) throws NoSuchEntityException {
        return this.service.update(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final LanguageType entity) {
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
    public List<LanguageType> getByLanguageAndType(String l, String t) {
        return this.service.getByLanguageAndType(l,t);
    }
}
