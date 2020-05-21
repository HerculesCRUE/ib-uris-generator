package es.um.asio.service.repository;

import es.um.asio.service.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
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
    Optional<Language> findByIso(String iso);

    /**
     * Finds a Language by isDefault.
     *
     * @param isDefault
     *            The isDefault to search for
     * @return an {@link Language} entity stored in the database or {@literal Optional#empty()} if none found
     */
    Optional<List<Language>> findByIsDefault(boolean isDefault);


}
