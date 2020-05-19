package es.um.asio.service.repository;

import es.um.asio.service.model.CanonicalURILanguage;
import es.um.asio.service.model.LocalURI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for {@link User}
 */
public interface LocalURIRepository extends JpaRepository<LocalURI, String>, JpaSpecificationExecutor<LocalURI> {
    /**
     * Finds a LocalURI using the fullURI field.
     *
     * @param fullURI
     *            The fullURI to search for
     * @return an {@link LocalURI} entity stored in the database or {@literal Optional#empty()} if none found
     */
    Optional<List<LocalURI>> findByLocalURI(String localUri);

    /**
     * Finds a LocalURI using the fullURI field.
     *
     * @param storageTypeStr
     *            The storageTypeStr to search for
     * @param canonicalURILanguageStr
     *            The canonicalURILanguageStr to search for
     * @return an {@link LocalURI} entity stored in the database or {@literal Optional#empty()} if none found
     */
    Optional<List<LocalURI>> findByStorageTypeStrAndCanonicalURILanguageStr(String storageTypeStr, String canonicalURILanguageStr);


}
