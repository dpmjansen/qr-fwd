package nl.daveit.qrforward.domain.service;

import nl.daveit.qrforward.domain.api.RedirectApi;
import nl.daveit.qrforward.domain.model.SimpleRedirectConfiguration;
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
    public List<SimpleRedirectConfiguration> getAllRedirectConfigurations() {
        return redirectStore.getAllRedirectConfigurations();
    }

    @Override
    public boolean addNewConfiguration(SimpleRedirectConfiguration redirectConfiguration) {
        return redirectStore.addNewConfiguration(redirectConfiguration);
    }

    @Override
    public Optional<SimpleRedirectConfiguration> getRedirectConfiguration(RedirectKey redirectKey) {
        return redirectStore.getRedirectConfiguration(redirectKey);
    }

    @Override
    public boolean updateRedirectConfiguration(SimpleRedirectConfiguration redirectConfiguration) {
        return redirectStore.updateRedirectConfiguration(redirectConfiguration);
    }

    @Override
    public boolean deleteRedirectConfiguration(RedirectKey redirectKey) {
        return redirectStore.deleteRedirectConfiguration(redirectKey);
    }
}
