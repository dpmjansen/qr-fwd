package nl.daveit.qrforward.persistence.service;

import nl.daveit.qrforward.domain.model.RedirectKey;
import nl.daveit.qrforward.domain.spi.RedirectStore;
import nl.daveit.qrforward.persistence.model.RedirectConfiguration;
import nl.daveit.qrforward.persistence.repository.RedirectConfigurationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RedirectStoreService implements RedirectStore {

    private final RedirectConfigurationRepository repository;

    public RedirectStoreService(RedirectConfigurationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<nl.daveit.qrforward.domain.model.RedirectConfiguration> getById(RedirectKey redirectKey) {
        return repository.findByUuid(redirectKey.key())
                .map(this::toDomain);
    }

    @Override
    public List<nl.daveit.qrforward.domain.model.RedirectConfiguration> getAllRedirectConfigurations() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    private nl.daveit.qrforward.domain.model.RedirectConfiguration toDomain(RedirectConfiguration persistence) {
        return new nl.daveit.qrforward.domain.model.RedirectConfiguration(persistence.getUuid(), persistence.getDestinationUrl(), persistence.getRedirectType(), persistence.getEnabled(), persistence.getDescription());
    }
}
