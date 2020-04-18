package es.um.asio.service.repository;

import es.um.asio.service.model.CanonicalURI;
import es.um.asio.service.model.CanonicalURILanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for {@link User}
 */
public interface CanonicalURILanguageRepository extends JpaRepository<CanonicalURILanguage, String>, JpaSpecificationExecutor<CanonicalURILanguage> {
    /**
     * Finds a CanonicalURI using the fullURI field.
     *
     * @param fullURI
     *            The fullURI to search for
     * @return an {@link CanonicalURILanguage} entity stored in the database or {@literal Optional#empty()} if none found
     */
    Optional<CanonicalURILanguage> findByFullURI(String fullURI);

    /**
     * Finds a CanonicalURILanguage using the entityName field and propertyName field.
     *
     * @param entityName
     *            The entityName to search for
     * @param propertyName
     *            The propertyName to search for
     * @return an {@link CanonicalURILanguage} entity stored in the database or {@literal Optional#empty()} if none found
     */
    Optional<List<CanonicalURILanguage>> findByEntityNameAndPropertyName(String entityName, String propertyName);

    /**
     * Finds a CanonicalURI using the entityName field and propertyName field.
     *
     * @param entityName
     *            The entityName to search for
     * @param propertyName
     *            The propertyName to search for
     * @return an {@link CanonicalURILanguage} entity stored in the database or {@literal Optional#empty()} if none found
     */
    Optional<List<CanonicalURILanguage>> findByEntityNameAndReference(String entityName, String reference);

    /**
     * Finds a CanonicalURI using  propertyName field.
     *
     * @param entityName
     *            The entityName to search for
     * @return an {@link CanonicalURILanguage} entity stored in the database or {@literal Optional#empty()} if none found
     */
    Optional<List<CanonicalURILanguage>> findByEntityName(String entityName);

    /**
     * Finds a CanonicalURI using  propertyName field.
     *
     * @param propertyName
     *            The propertyName to search for
     * @return an {@link CanonicalURILanguage} entity stored in the database or {@literal Optional#empty()} if none found
     */
    Optional<List<CanonicalURILanguage>> findByPropertyName(String propertyName);


}
