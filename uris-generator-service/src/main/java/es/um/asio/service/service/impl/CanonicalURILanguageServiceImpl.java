package es.um.asio.service.service.impl;

import es.um.asio.service.filter.CanonicalURIFilter;
import es.um.asio.service.filter.CanonicalURILanguageFilter;
import es.um.asio.service.model.CanonicalURI;
import es.um.asio.service.model.CanonicalURILanguage;
import es.um.asio.service.model.User;
import es.um.asio.service.repository.CanonicalURILanguageRepository;
import es.um.asio.service.repository.CanonicalURIRepository;
import es.um.asio.service.service.CanonicalURILanguageService;
import es.um.asio.service.service.CanonicalURIService;
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
public class CanonicalURILanguageServiceImpl implements CanonicalURILanguageService {

    /**
     * Spring Data repository for {@link CanonicalURI}.
     */
    @Autowired
    private CanonicalURILanguageRepository repository;

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
    public CanonicalURILanguage save(final CanonicalURILanguage entity) {
        return this.repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public List<CanonicalURILanguage> save(final Iterable<CanonicalURILanguage> entities) {
        return this.repository.saveAll(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public CanonicalURILanguage update(final CanonicalURILanguage entity) {
        return this.repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void delete(final CanonicalURILanguage entity) {
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
    public Optional<CanonicalURILanguage> find(final String identifier) {
        return this.repository.findById(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CanonicalURILanguage> findPaginated(final CanonicalURILanguageFilter filter, final Pageable pageable) {
        return this.repository.findAll(filter, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CanonicalURILanguage> findAll() {
        return this.repository.findAll();
    }

    @Override
    public List<CanonicalURILanguage> getAllByCanonicalURILanguage(CanonicalURILanguage entity) {
        CanonicalURILanguageFilter f = entity.buildFilterByEntityUniqueProperties();

        if (f.getList().size()>0) {
            List<CanonicalURILanguage> filteredList = this.repository.findAll(f);
            return filteredList;
        } else {
            return new ArrayList<CanonicalURILanguage>();
        }
    }
}
