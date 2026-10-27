package nl.daveit.qrforward.persistence.service;

import nl.daveit.qrforward.domain.model.RedirectConfiguration;
import nl.daveit.qrforward.domain.model.RedirectKey;
import nl.daveit.qrforward.domain.spi.RedirectStore;
import nl.daveit.qrforward.persistence.repository.RedirectConfigurationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class RedirectPersistenceService implements RedirectStore{

    private final RedirectConfigurationRepository repository;

    public RedirectPersistenceService(RedirectConfigurationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<RedirectConfiguration> getAllRedirectConfigurations() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public boolean addNewConfiguration(RedirectConfiguration redirectConfiguration) {
        var persistence = toPersistence(redirectConfiguration);
        Instant now = Instant.now();
        persistence.setCreatedAt(now);
        persistence.setUpdatedAt(now);
        repository.save(persistence);
        return true;
    }

    @Override
    public Optional<RedirectConfiguration> getRedirectConfiguration(RedirectKey redirectKey) {
        return repository.findByUuid(redirectKey.key()).map(this::toDomain);
    }

    @Override
    public boolean updateRedirectConfiguration(RedirectConfiguration redirectConfiguration) {
        var config = repository.findByUuid(redirectConfiguration.getRedirectKey().key());

        if(config.isEmpty()){
            return false;
        }

        var persistenceConfig = config.get();
        persistenceConfig.setDestinationUrl(redirectConfiguration.getDestinationUrl());
        persistenceConfig.setRedirectType(redirectConfiguration.getRedirectType());
        persistenceConfig.setEnabled(redirectConfiguration.getEnabled());
        persistenceConfig.setDescription(redirectConfiguration.getDescription());
        persistenceConfig.setUpdatedAt(Instant.now());

        repository.save(persistenceConfig);
        return true;
    }

    @Override
    public boolean deleteRedirectConfiguration(RedirectKey redirectKey) {
        var result = repository.deleteByUuid(redirectKey.key());
        return result.isPresent();
    }

    private RedirectConfiguration toDomain(nl.daveit.qrforward.persistence.model.RedirectConfiguration persistence) {
        return new RedirectConfiguration(new RedirectKey(persistence.getUuid()), persistence.getDestinationUrl(), persistence.getRedirectType(), persistence.getEnabled(), persistence.getDescription());
    }

    private nl.daveit.qrforward.persistence.model.RedirectConfiguration toPersistence(RedirectConfiguration domain) {
        return new nl.daveit.qrforward.persistence.model.RedirectConfiguration(domain.getRedirectKey().key(), domain.getDestinationUrl(), domain.getRedirectType(), domain.getEnabled(), domain.getDescription(),null,null);
    }
}
