package es.um.asio.service.repository;

import es.um.asio.service.model.LocalURI;
import es.um.asio.service.model.StorageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Spring Data repository for {@link User}
 */
public interface StorageTypeRepository extends JpaRepository<StorageType, String>, JpaSpecificationExecutor<StorageType> {
    /**
     * Finds a StorageType using the fullURI field.
     *
     * @param name
     *            The name to search for
     * @return an {@link StorageType} entity stored in the database or {@literal Optional#empty()} if none found
     */
    Optional<StorageType> findByName(String name);


}
