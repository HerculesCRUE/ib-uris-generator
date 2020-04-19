package es.um.asio.service.repository;

import es.um.asio.service.model.CanonicalURI;
import es.um.asio.service.model.Type;
import es.um.asio.service.model.URIMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Spring Data repository for {@link User}
 */
public interface TypeRepository extends JpaRepository<Type, String>, JpaSpecificationExecutor<Type> {
    /**
     * Finds a Type using the username field.
     *
     * @param code
     *            The code to search for
     * @return an {@link Type} entity stored in the database or {@literal Optional#empty()} if none found
     */
    Optional<Type> findByCode(String code);


}
