package nl.daveit.qrforward.domain.service;

import nl.daveit.qrforward.domain.api.RedirectApi;
import nl.daveit.qrforward.domain.model.RedirectConfiguration;
import nl.daveit.qrforward.domain.model.RedirectKey;
import nl.daveit.qrforward.domain.spi.RedirectStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RedirectService implements RedirectApi {

    private final RedirectStore redirectStore;

    public RedirectService(RedirectStore redirectStore) {
        this.redirectStore = redirectStore;
    }

    @Override
    public List<RedirectConfiguration> getAllRedirectConfigurations() {
        return redirectStore.getAllRedirectConfigurations();
    }

    @Override
    public boolean addNewConfiguration(RedirectConfiguration redirectConfiguration) {
        return redirectStore.addNewConfiguration(redirectConfiguration);
    }

    @Override
    public Optional<RedirectConfiguration> getRedirectConfiguration(RedirectKey redirectKey) {
        return redirectStore.getRedirectConfiguration(redirectKey);
    }

    @Override
    public boolean updateRedirectConfiguration(RedirectConfiguration redirectConfiguration) {
        return redirectStore.updateRedirectConfiguration(redirectConfiguration);
    }

    @Override
    public boolean deleteRedirectConfiguration(RedirectKey redirectKey) {
        return redirectStore.deleteRedirectConfiguration(redirectKey);
    }
}
