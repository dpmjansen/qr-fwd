package nl.daveit.qrforward.domain.api;

import nl.daveit.qrforward.domain.model.SimpleRedirectConfiguration;
import nl.daveit.qrforward.domain.model.RedirectKey;

import java.util.Optional;

public interface RedirectApi {

    Optional<SimpleRedirectConfiguration> getRedirectConfiguration(RedirectKey redirectKey);

}
