package es.um.asio.service.service.impl;

import es.um.asio.service.filter.URIMapFilter;
import es.um.asio.service.model.URIMap;
import es.um.asio.service.model.User;
import es.um.asio.service.repository.URIMapRepository;
import es.um.asio.service.service.URIMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation to handle {@link User} entity related operations
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class URIMapServiceImpl implements URIMapService {

    /**
     * Spring Data repository for {@link URIMap}.
     */
    @Autowired
    private URIMapRepository uriMapRepository;

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
    public URIMap save(final URIMap entity) {
        return this.uriMapRepository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public List<URIMap> save(final Iterable<URIMap> entities) {
        return this.uriMapRepository.saveAll(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public URIMap update(final URIMap entity) {
        return this.uriMapRepository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void delete(final URIMap entity) {
        this.uriMapRepository.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void delete(final String identifier) {
        this.uriMapRepository.deleteById(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<URIMap> find(final String identifier) {
        return this.uriMapRepository.findById(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<URIMap> findPaginated(final URIMapFilter filter, final Pageable pageable) {
        return this.uriMapRepository.findAll(filter, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<URIMap> findAll() {
        return this.uriMapRepository.findAll();
    }

    @Override
    public List<URIMap> getAllByURIMap(URIMap uriMap) {
        URIMapFilter f = uriMap.buildFilterByEntityUniqueProperties();

        List<URIMap> filteredList = this.uriMapRepository.findAll(f);

        return filteredList;
    }
}
