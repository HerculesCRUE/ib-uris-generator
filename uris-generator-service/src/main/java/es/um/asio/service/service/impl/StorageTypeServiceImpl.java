package es.um.asio.service.service.impl;

import es.um.asio.service.filter.LocalURIFilter;
import es.um.asio.service.filter.StorageTypeFilter;
import es.um.asio.service.model.CanonicalURI;
import es.um.asio.service.model.LocalURI;
import es.um.asio.service.model.StorageType;
import es.um.asio.service.model.User;
import es.um.asio.service.repository.LocalURIRepository;
import es.um.asio.service.repository.StorageTypeRepository;
import es.um.asio.service.service.LocalURIService;
import es.um.asio.service.service.StorageTypeService;
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
public class StorageTypeServiceImpl implements StorageTypeService {

    /**
     * Spring Data repository for {@link CanonicalURI}.
     */
    @Autowired
    private StorageTypeRepository repository;

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
    public StorageType save(final StorageType entity) {
        return this.repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public List<StorageType> save(final Iterable<StorageType> entities) {
        return this.repository.saveAll(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public StorageType update(final StorageType entity) {
        return this.repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void delete(final StorageType entity) {
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
    public Optional<StorageType> find(final String identifier) {
        return this.repository.findById(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<StorageType> findPaginated(final StorageTypeFilter filter, final Pageable pageable) {
        return this.repository.findAll(filter, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StorageType> findAll() {
        return this.repository.findAll();
    }

    @Override
    public List<StorageType> getAllByStorageType(StorageType entity) {
        StorageTypeFilter f = entity.buildFilterByEntityUniqueProperties();

        if (f.getList().size()>0) {
            List<StorageType> filteredList = this.repository.findAll(f);
            return filteredList;
        } else {
            return new ArrayList<StorageType>();
        }
    }

    @Override
    public StorageType getAllByName(String name) {
        return this.repository.findByName(name).orElse(null);
    }

    @Override
    public void deleteByName(String name) {
        StorageType st = this.repository.findByName(name).orElse(null);
        if (st!=null)
            this.repository.delete(st);
    }
}
