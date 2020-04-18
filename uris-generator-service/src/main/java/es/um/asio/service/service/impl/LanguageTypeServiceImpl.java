package es.um.asio.service.service.impl;

import es.um.asio.service.filter.LanguageFilter;
import es.um.asio.service.filter.LanguageTypeFilter;
import es.um.asio.service.model.CanonicalURI;
import es.um.asio.service.model.Language;
import es.um.asio.service.model.LanguageType;
import es.um.asio.service.model.User;
import es.um.asio.service.repository.LanguageRepository;
import es.um.asio.service.repository.LanguageTypeRepository;
import es.um.asio.service.service.LanguageService;
import es.um.asio.service.service.LanguageTypeService;
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
public class LanguageTypeServiceImpl implements LanguageTypeService {

    /**
     * Spring Data repository for {@link CanonicalURI}.
     */
    @Autowired
    private LanguageTypeRepository repository;

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
    public LanguageType save(final LanguageType entity) {
        return this.repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public List<LanguageType> save(final Iterable<LanguageType> entities) {
        return this.repository.saveAll(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public LanguageType update(final LanguageType entity) {
        return this.repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void delete(final LanguageType entity) {
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
    public Optional<LanguageType> find(final String identifier) {
        return this.repository.findById(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<LanguageType> findPaginated(final LanguageTypeFilter filter, final Pageable pageable) {
        return this.repository.findAll(filter, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LanguageType> findAll() {
        return this.repository.findAll();
    }

    @Override
    public List<LanguageType> getAllByLanguageType(LanguageType entity) {
        LanguageTypeFilter f = entity.buildFilterByEntityUniqueProperties();

        if (f.getList().size()>0) {
            List<LanguageType> filteredList = this.repository.findAll(f);
            return filteredList;
        } else {
            return new ArrayList<LanguageType>();
        }
    }

    @Override
    public List<LanguageType> getByLanguageAndType(String l, String t) {
        List<LanguageType> languageTypes = new ArrayList<>();
        for (LanguageType lt : this.repository.findAll()) {
            if (
                    ( l == null || (lt.getLanguage()!=null && lt.getLanguage()!=null && lt.getLanguage().getISO().trim().equals(l.trim()))) &&
                    ( t == null || (lt.getType()!=null && lt.getType().getCode()!=null &&lt.getType().getCode().trim().equals(t.trim())))
            ) {
                languageTypes.add(lt);
            }
        }
        return languageTypes;
    }
}
