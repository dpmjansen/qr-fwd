package nl.daveit.qrforward.domain.spi;

import nl.daveit.qrforward.domain.model.RedirectConfiguration;
import nl.daveit.qrforward.domain.model.RedirectKey;

import java.util.List;
import java.util.Optional;

public interface RedirectStore {

    List<RedirectConfiguration> getAllRedirectConfigurations();
    boolean addNewConfiguration(RedirectConfiguration redirectConfiguration);
    Optional<RedirectConfiguration> getRedirectConfiguration(RedirectKey redirectKey);
    boolean updateRedirectConfiguration(RedirectConfiguration redirectConfiguration);
    boolean deleteRedirectConfiguration(RedirectKey redirectKey);

}
