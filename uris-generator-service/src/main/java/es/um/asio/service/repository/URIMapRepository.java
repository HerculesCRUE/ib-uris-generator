package es.um.asio.service.repository;

import es.um.asio.service.model.URIMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Spring Data repository for {@link User}
 */
public interface URIMapRepository extends JpaRepository<URIMap, String>, JpaSpecificationExecutor<URIMap> {
    /**
     * Finds a user using the username field.
     *
     * @param ontologyURI
     *            The ontologyURI to search for
     * @return an {@link URIMap} entity stored in the database or {@literal Optional#empty()} if none found
     */
    Optional<URIMap> findByOntologyURI(String ontologyURI);

    /**
     * Locks / unlocks a user account.
     *
     * @param accountNonLocked
     *            The accountNonLocked value to set
     * @param userId
     *            The userId that is being modified
     */
    @Modifying
    @Query("update User u set u.accountNonLocked = ?1 where u.id = ?2")
    void setAccountNonLocked(boolean accountNonLocked, String userId);

}
