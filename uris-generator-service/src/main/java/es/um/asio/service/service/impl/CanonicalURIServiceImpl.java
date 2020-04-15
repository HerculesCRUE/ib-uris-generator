package es.um.asio.service.service.impl;

import es.um.asio.service.filter.CanonicalURIFilter;
import es.um.asio.service.filter.CanonicalURILanguageFilter;
import es.um.asio.service.filter.URIMapFilter;
import es.um.asio.service.model.CanonicalURI;
import es.um.asio.service.model.URIMap;
import es.um.asio.service.model.User;
import es.um.asio.service.repository.CanonicalURIRepository;
import es.um.asio.service.repository.URIMapRepository;
import es.um.asio.service.service.CanonicalURILanguageService;
import es.um.asio.service.service.CanonicalURIService;
import es.um.asio.service.service.URIMapService;
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
public class CanonicalURIServiceImpl implements CanonicalURIService {

    /**
     * Spring Data repository for {@link CanonicalURI}.
     */
    @Autowired
    private CanonicalURIRepository repository;

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
    public CanonicalURI save(final CanonicalURI entity) {
        return this.repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public List<CanonicalURI> save(final Iterable<CanonicalURI> entities) {
        return this.repository.saveAll(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public CanonicalURI update(final CanonicalURI entity) {
        return this.repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void delete(final CanonicalURI entity) {
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
    public Optional<CanonicalURI> find(final String identifier) {
        return this.repository.findById(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CanonicalURI> findPaginated(final CanonicalURIFilter filter, final Pageable pageable) {
        return this.repository.findAll(filter, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CanonicalURI> findAll() {
        return this.repository.findAll();
    }

    @Override
    public List<CanonicalURI> getAllByCanonicalURI(CanonicalURI entity) {
        CanonicalURIFilter f = entity.buildFilterByEntityUniqueProperties();

        if (f.getList().size()>0) {
            List<CanonicalURI> filteredList = this.repository.findAll(f);
            return filteredList;
        } else {
            return new ArrayList<CanonicalURI>();
        }
    }
}
