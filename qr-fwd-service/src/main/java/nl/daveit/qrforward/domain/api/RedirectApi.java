package nl.daveit.qrforward.domain.api;

import nl.daveit.qrforward.domain.model.RedirectConfiguration;
import nl.daveit.qrforward.domain.model.RedirectKey;

import java.util.Optional;

public interface RedirectApi {

    void loadAllConfigurationsIntoCache();

    Optional<RedirectConfiguration> getRedirectConfiguration(RedirectKey redirectKey);

}
