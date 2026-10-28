package nl.daveit.qrforward.client.redirectapi.service;

import nl.daveit.qrforward.domain.model.RedirectConfiguration;
import nl.daveit.qrforward.domain.model.RedirectKey;
import nl.daveit.qrforward.domain.spi.RedirectConfigurationProvider;
import nl.daveit.qrforward.openapi.model.RedirectConfigurationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;
import java.util.Optional;

@Service
public class RedirectApiClient implements RedirectConfigurationProvider {

    private final RedirectApiService client;

    public RedirectApiClient(RedirectApiService client) {
        this.client = client;
    }

    @Override
    public Optional<RedirectConfiguration> getById(RedirectKey redirectKey) {
        var configuration = client.getConfiguration(redirectKey.key());
        return Optional.ofNullable(configuration).map(this::toDomain);
    }

    @Override
    public List<RedirectConfiguration> getAllRedirectConfigurations() {
        return client.getAllConfigurations().stream().map(this::toDomain).toList();
    }

    private RedirectConfiguration toDomain(RedirectConfigurationResponse config) {
        var domain = new RedirectConfiguration();
        domain.setRedirectKey(new RedirectKey(config.getUuid()));
        domain.setDestinationUrl(config.getDestinationUrl());
        domain.setDescription(config.getDescription());
        domain.setRedirectType(config.getRedirectType().getValue());
        domain.setEnabled(config.getEnabled());
        return domain;
    }
}
