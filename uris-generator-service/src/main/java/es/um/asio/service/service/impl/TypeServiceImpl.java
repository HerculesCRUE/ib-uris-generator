package es.um.asio.service.service.impl;

import es.um.asio.service.filter.StorageTypeFilter;
import es.um.asio.service.filter.TypeFilter;
import es.um.asio.service.model.CanonicalURI;
import es.um.asio.service.model.StorageType;
import es.um.asio.service.model.Type;
import es.um.asio.service.model.User;
import es.um.asio.service.repository.StorageTypeRepository;
import es.um.asio.service.repository.TypeRepository;
import es.um.asio.service.service.StorageTypeService;
import es.um.asio.service.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation to handle {@link User} entity related operations
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TypeServiceImpl implements TypeService {

    /**
     * Spring Data repository for {@link CanonicalURI}.
     */
    @Autowired
    private TypeRepository repository;

    /**
     * Solr enabled
     */
    @Value("${app.solr.enabled:#{false}}")
    private boolean solrEnabled;

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public Type save(final Type entity) {
        return this.repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public List<Type> save(final Iterable<Type> entities) {
        return this.repository.saveAll(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public Type update(final Type entity) {
        return this.repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void delete(final Type entity) {
        this.repository.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void delete(final String identifier) {
        this.repository.deleteById(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Type> find(final String identifier) {
        return this.repository.findById(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Type> findPaginated(final TypeFilter filter, final Pageable pageable) {
        return this.repository.findAll(filter, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Type> findAll() {
        return this.repository.findAll();
    }

    @Override
    public List<Type> getAllByType(Type entity) {
        TypeFilter f = entity.buildFilterByEntityUniqueProperties();

        if (f.getList().size()>0) {
            List<Type> filteredList = this.repository.findAll(f);
            return filteredList;
        } else {
            return new ArrayList<Type>();
        }
    }

}
