package nl.daveit.qrforward.persistence.repository;

import nl.daveit.qrforward.persistence.model.RedirectConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for accessing RedirectConfiguration entities.
 * Provides database operations for managing redirect configurations by UUID.
 */
@Repository
public interface RedirectConfigurationRepository extends JpaRepository<RedirectConfiguration, Long> {

    Optional<RedirectConfiguration> findByUuid(UUID uuid);

    Optional<RedirectConfiguration> deleteByUuid(UUID uuid);

}
