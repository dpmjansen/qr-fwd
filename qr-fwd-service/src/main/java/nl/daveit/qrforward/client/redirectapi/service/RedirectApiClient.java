package nl.daveit.qrforward.client.redirectapi.service;

import nl.daveit.qrforward.domain.model.SimpleRedirectConfiguration;
import nl.daveit.qrforward.domain.model.RedirectKey;
import nl.daveit.qrforward.domain.spi.RedirectConfigurationProvider;
import nl.daveit.qrforward.openapi.model.RedirectConfigurationResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RedirectApiClient implements RedirectConfigurationProvider {

    private final RedirectApiService client;

    public RedirectApiClient(RedirectApiService client) {
        this.client = client;
    }

    @Override
    public Optional<SimpleRedirectConfiguration> getById(RedirectKey redirectKey) {
        var configuration = client.getConfiguration(redirectKey.key());
        return Optional.ofNullable(configuration).map(this::toDomain);
    }

    @Override
    public List<SimpleRedirectConfiguration> getAllRedirectConfigurations() {
        return client.getAllConfigurations().stream().map(this::toDomain).toList();
    }

    private SimpleRedirectConfiguration toDomain(RedirectConfigurationResponse config) {
        var domain = new SimpleRedirectConfiguration();
        domain.setRedirectKey(new RedirectKey(config.getUuid()));
        domain.setDestinationUrl(config.getDestinationUrl());
        domain.setDescription(config.getDescription());
        domain.setRedirectType(config.getRedirectType().getValue());
        domain.setEnabled(config.getEnabled());
        return domain;
    }
}
