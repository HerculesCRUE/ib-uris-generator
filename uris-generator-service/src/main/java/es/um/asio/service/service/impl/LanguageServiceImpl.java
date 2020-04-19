package es.um.asio.service.service.impl;

import es.um.asio.service.filter.CanonicalURIFilter;
import es.um.asio.service.filter.LanguageFilter;
import es.um.asio.service.model.CanonicalURI;
import es.um.asio.service.model.Language;
import es.um.asio.service.model.User;
import es.um.asio.service.repository.CanonicalURIRepository;
import es.um.asio.service.repository.LanguageRepository;
import es.um.asio.service.service.CanonicalURIService;
import es.um.asio.service.service.LanguageService;
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
public class LanguageServiceImpl implements LanguageService {

    /**
     * Spring Data repository for {@link CanonicalURI}.
     */
    @Autowired
    private LanguageRepository repository;

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
    public Language save(final Language entity) {
        return this.repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public List<Language> save(final Iterable<Language> entities) {
        return this.repository.saveAll(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public Language update(final Language entity) {
        return this.repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void delete(final Language entity) {
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
    public Optional<Language> find(final String identifier) {
        return this.repository.findById(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Language> findPaginated(final LanguageFilter filter, final Pageable pageable) {
        return this.repository.findAll(filter, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Language> findAll() {
        return this.repository.findAll();
    }

    @Override
    public List<Language> getAllByLanguage(Language entity) {
        LanguageFilter f = entity.buildFilterByEntityUniqueProperties();

        if (f.getList().size()>0) {
            List<Language> filteredList = this.repository.findAll(f);
            return filteredList;
        } else {
            return new ArrayList<Language>();
        }
    }

    @Override
    public void setNotIsDefaultAllLanguages() {
        Optional<List<Language>> languages = this.repository.findByIsDefault(true);
        languages.ifPresent((ls)->ls.forEach(
                (l)-> {
                    l.isDefault = false;
                    this.repository.save(l);
                }
        ));

    }

    @Override
    public List<Language> getDefaultLanguages() {
        return this.repository.findByIsDefault(true).orElse(new ArrayList<>());
    }
}
