package es.um.asio.service.repository;

import es.um.asio.service.model.Language;
import es.um.asio.service.model.Type;
import es.um.asio.service.model.URIMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Spring Data repository for {@link User}
 */
public interface LanguageRepository extends JpaRepository<Language, String>, JpaSpecificationExecutor<Language> {
    /**
     * Finds a Language using the username field.
     *
     * @param code
     *            The iso to search for
     * @return an {@link Language} entity stored in the database or {@literal Optional#empty()} if none found
     */
    Optional<Language> findByISO(String iso);


}
