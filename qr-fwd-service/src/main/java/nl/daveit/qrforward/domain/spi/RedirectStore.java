package nl.daveit.qrforward.domain.spi;

import nl.daveit.qrforward.domain.model.RedirectKey;
import nl.daveit.qrforward.domain.model.RedirectConfiguration;

import java.util.List;
import java.util.Optional;

public interface RedirectStore {

    Optional<RedirectConfiguration> getById(RedirectKey redirectKey);

    List<RedirectConfiguration> getAllRedirectConfigurations();
}
