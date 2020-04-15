package es.um.asio.service.repository;

import es.um.asio.service.model.CanonicalURI;
import es.um.asio.service.model.URIMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Spring Data repository for {@link User}
 */
public interface CanonicalURIRepository extends JpaRepository<CanonicalURI, String>, JpaSpecificationExecutor<CanonicalURI> {
    /**
     * Finds a CanonicalURI using the username field.
     *
     * @param fullURI
     *            The ontologyURI to search for
     * @return an {@link CanonicalURI} entity stored in the database or {@literal Optional#empty()} if none found
     */
    Optional<CanonicalURI> findByFullURI(String fullURI);


}
