package nl.daveit.qrforward.domain.api;


import nl.daveit.qrforward.domain.model.SimpleRedirectConfiguration;
import nl.daveit.qrforward.domain.model.RedirectKey;

import java.util.List;
import java.util.Optional;

public interface RedirectApi {

    List<SimpleRedirectConfiguration> getAllRedirectConfigurations();
    boolean addNewConfiguration(SimpleRedirectConfiguration redirectConfiguration);
    Optional<SimpleRedirectConfiguration> getRedirectConfiguration(RedirectKey redirectKey);
    boolean updateRedirectConfiguration(SimpleRedirectConfiguration redirectConfiguration);
    boolean deleteRedirectConfiguration(RedirectKey redirectKey);

}
