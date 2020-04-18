package es.um.asio.service.proxy.impl;

import com.izertis.abstractions.exception.NoSuchEntityException;
import es.um.asio.service.filter.LanguageFilter;
import es.um.asio.service.filter.TypeFilter;
import es.um.asio.service.model.Language;
import es.um.asio.service.model.Type;
import es.um.asio.service.model.User;
import es.um.asio.service.proxy.LanguageProxy;
import es.um.asio.service.proxy.TypeProxy;
import es.um.asio.service.service.LanguageService;
import es.um.asio.service.service.TypeService;
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
public class TypeProxyImpl implements TypeProxy {



    /**
     * Service layer.
     */
    @Autowired
    private TypeService service;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Type> find(final String identifier) {
        return this.service.find(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Type> findPaginated(final TypeFilter filter, final Pageable pageable) {
        return this.service.findPaginated(filter,pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Type> findAll() {
        return this.service.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type save(final Type entity) {
        List<Type> filtered = this.service.getAllByType(entity);
        if (filtered.size() == 0) {
            return this.service.save(entity);
        } else {
            for (Type e :filtered) {
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
    public List<Type> save(final Iterable<Type> entities) {
        return this.service.save(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type update(final Type entity) throws NoSuchEntityException {
        return this.service.update(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final Type entity) {
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
    public Type findOrCreate(String type) {
        Type t = find(type).orElse(null);
        if (t == null) {
            t = new Type(type,type);
            save(t);
        }
        return t;
    }
}
