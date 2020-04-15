package es.um.asio.service.repository;

import es.um.asio.service.model.Language;
import es.um.asio.service.model.LanguageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for {@link User}
 */
public interface LanguageTypeRepository extends JpaRepository<LanguageType, String>, JpaSpecificationExecutor<LanguageType> {
    /**
     * Finds a LanguageType using the Language field.
     *
     * @param iso
     *            The iso to search for
     * @return an {@link LanguageType} entity stored in the database or {@literal Optional#empty()} if none found
     */
    Optional<List<LanguageType>> findByLanguage(String iso);

    /**
     * Finds a LanguageType using the type field.
     *
     * @param type
     *            The iso to search for
     * @return an {@link LanguageType} entity stored in the database or {@literal Optional#empty()} if none found
     */
    Optional<List<LanguageType>> findByType(String type);

    /**
     * Finds a LanguageType using the Language and Type fields.
     *
     * @param iso
     *            The iso to search for
     * @param type
     *             The type to search for
     * @return an {@link LanguageType} entity stored in the database or {@literal Optional#empty()} if none found
     */
    Optional<List<LanguageType>> findByLanguageAndType(String iso,String type);


}
