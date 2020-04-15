package es.um.asio.service.service.impl;

import es.um.asio.service.filter.LocalURIFilter;
import es.um.asio.service.model.CanonicalURI;

import es.um.asio.service.model.LocalURI;
import es.um.asio.service.model.User;

import es.um.asio.service.repository.LocalURIRepository;

import es.um.asio.service.service.LocalURIService;
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
public class LocalURIServiceImpl implements LocalURIService {

    /**
     * Spring Data repository for {@link CanonicalURI}.
     */
    @Autowired
    private LocalURIRepository repository;


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
    public LocalURI save(final LocalURI entity) {
        return this.repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public List<LocalURI> save(final Iterable<LocalURI> entities) {
        return this.repository.saveAll(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public LocalURI update(final LocalURI entity) {
        return this.repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void delete(final LocalURI entity) {
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
    public Optional<LocalURI> find(final String identifier) {
        return this.repository.findById(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<LocalURI> findPaginated(final LocalURIFilter filter, final Pageable pageable) {
        return this.repository.findAll(filter, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocalURI> findAll() {
        return this.repository.findAll();
    }

    @Override
    public List<LocalURI> getAllByLocalURI(LocalURI entity) {
        LocalURIFilter f = entity.buildFilterByEntityUniqueProperties();

        if (f.getList().size()>0) {
            List<LocalURI> filteredList = this.repository.findAll(f);
            return filteredList;
        } else {
            return new ArrayList<LocalURI>();
        }
    }

}
