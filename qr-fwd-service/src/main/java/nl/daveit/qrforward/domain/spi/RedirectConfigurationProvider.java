package nl.daveit.qrforward.domain.spi;

import nl.daveit.qrforward.domain.model.RedirectKey;
import nl.daveit.qrforward.domain.model.SimpleRedirectConfiguration;

import java.util.List;
import java.util.Optional;

public interface RedirectConfigurationProvider {

    Optional<SimpleRedirectConfiguration> getById(RedirectKey redirectKey);

    List<SimpleRedirectConfiguration> getAllRedirectConfigurations();
}
