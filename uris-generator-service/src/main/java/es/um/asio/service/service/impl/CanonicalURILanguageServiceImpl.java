package es.um.asio.service.service.impl;

import es.um.asio.service.filter.CanonicalURIFilter;
import es.um.asio.service.filter.CanonicalURILanguageFilter;
import es.um.asio.service.filter.SearchCriteria;
import es.um.asio.service.filter.SearchOperation;
import es.um.asio.service.model.CanonicalURI;
import es.um.asio.service.model.CanonicalURILanguage;
import es.um.asio.service.model.User;
import es.um.asio.service.repository.CanonicalURILanguageRepository;
import es.um.asio.service.service.CanonicalURILanguageService;
import es.um.asio.service.util.Utils;
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
     * Spring Data repository for {@link CanonicalURILanguage}.
     */
    @Autowired
    private CanonicalURILanguageRepository repository;

    /**
     * Spring Data repository for {@link CanonicalURI}.
     */
    @Autowired
    private CanonicalURILanguageRepository canonicalRepository;

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
        if (entity.getIsEntity()) {
            return this.repository.findByEntityNameAndIsEntity(entity.getEntityName(),true).orElse(new ArrayList<>());
        } else if (entity.getIsProperty()) {
            return this.repository.findByPropertyNameAndIsProperty(entity.getPropertyName(),true).orElse(new ArrayList<>());
        } else {
            return this.repository.findByEntityNameAndReference(entity.getEntityName(),entity.getReference()).orElse(new ArrayList<>());
        }
    }

    @Override
    public CanonicalURILanguage getAllByFullURI(String fullURI) {
        return this.repository.findByFullURI(fullURI).orElse(null);
    }

    @Override
    public List<CanonicalURILanguage> getAllByEntityNameAndPropertyName(String entityName, String propertyName) {
        return this.repository.findByEntityNameAndPropertyName(entityName,propertyName).orElse(new ArrayList<>());
    }

    @Override
    public List<CanonicalURILanguage> getAllByEntityNameAndReference(String entityName, String reference) {
        return this.repository.findByEntityNameAndReference(entityName,reference).orElse(
                this.repository.findByEntityNameAndLocalId(entityName,reference).orElse(new ArrayList<>())
        );
    }

    @Override
    public List<CanonicalURILanguage> getAllByEntityName(String entityName) {
        return this.repository.findByEntityName(entityName).orElse(new ArrayList<>());
    }

    @Override
    public List<CanonicalURILanguage> getAllByEntityNameAndIsEntity(String entityName) {
        return this.repository.findByEntityNameAndIsEntity(entityName,true).orElse(new ArrayList<>());
    }

    @Override
    public List<CanonicalURILanguage> getAllByPropertyName(String propertyName) {
        return this.repository.findByPropertyName(propertyName).orElse(new ArrayList<>());
    }

    @Override
    public List<CanonicalURILanguage> getAllByPropertyNameAndIsProperty(String propertyName) {
        return this.repository.findByPropertyNameAndIsProperty(propertyName,true).orElse(new ArrayList<>());
    }

    @Override
    public List<CanonicalURILanguage> getAllByElements(String domain, String subDomain, String language, String type, String concept, String reference) {
        List<CanonicalURILanguage> canonicalURILanguages = new ArrayList<>();

        CanonicalURILanguageFilter f = new CanonicalURILanguageFilter();

        if (Utils.isValidString(domain)) {
            f.add(new SearchCriteria("domain", domain, SearchOperation.EQUAL));
        }

        if (Utils.isValidString(subDomain)) {
            f.add(new SearchCriteria("subDomain", subDomain, SearchOperation.EQUAL));
        }

        if (Utils.isValidString(type)) {
            f.add(new SearchCriteria("typeCode", type, SearchOperation.EQUAL));
        }

        if (Utils.isValidString(language)) {
            f.add(new SearchCriteria("languageID", language, SearchOperation.EQUAL));
        }

        if (Utils.isValidString(concept)) {
            f.add(new SearchCriteria("concept", concept, SearchOperation.EQUAL));
        }

        if (Utils.isValidString(reference)) {
            f.add(new SearchCriteria("reference", reference, SearchOperation.EQUAL));
        } else {
            f.add(new SearchCriteria("reference", reference, SearchOperation.IS_NULL));
        }

        canonicalURILanguages = this.repository.findAll(f);
        return canonicalURILanguages;
    }


}
