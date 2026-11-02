package nl.daveit.qrforward.domain.service;

import nl.daveit.qrforward.domain.api.RedirectApi;
import nl.daveit.qrforward.domain.config.RedirectServiceProperties;
import nl.daveit.qrforward.domain.spi.RedirectConfigurationProvider;
import nl.daveit.qrforward.domain.model.SimpleRedirectConfiguration;
import nl.daveit.qrforward.domain.model.RedirectKey;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service for managing and retrieving redirect configurations by UUID.
 * Provides caching functionality for improved performance.
 */
@Service
public class RedirectService implements RedirectApi {

    private final RedirectConfigurationProvider redirectConfigurationProvider;
    private final CacheManager cacheManager;
    private final RedirectServiceProperties properties;

    public RedirectService(
            RedirectConfigurationProvider redirectConfigurationProvider,
            CacheManager cacheManager,
            RedirectServiceProperties properties) {
        this.redirectConfigurationProvider = redirectConfigurationProvider;
        this.cacheManager = cacheManager;
        this.properties = properties;

        loadAllConfigurationsIntoCache();
    }

    /**
     * Retrieves a redirect configuration by UUID.
     *
     * @param redirectKey the UUID to match
     * @return Optional containing the matching configuration, or empty if not found
     */
    @Override
    public Optional<SimpleRedirectConfiguration> getRedirectConfiguration(RedirectKey redirectKey) {
        Cache cache = getCache();
        if (cache != null) {
            SimpleRedirectConfiguration config = cache.get(redirectKey.key(), SimpleRedirectConfiguration.class);
            if (config != null) {
                return Optional.of(config);
            }
        }

        Optional<SimpleRedirectConfiguration> config = redirectConfigurationProvider.getById(redirectKey);

        config.ifPresent(c -> {
            Cache cacheToUse = getCache();
            if (cacheToUse != null && c.getRedirectKey() != null) {
                cacheToUse.put(c.getRedirectKey(), c);
            }
        });

        return config;
    }

    /**
     * Loads all redirect configurations into the cache.
     */
    private void loadAllConfigurationsIntoCache() {
        Cache cache = getCache();
        if (cache != null) {
            List<SimpleRedirectConfiguration> configurations = redirectConfigurationProvider.getAllRedirectConfigurations();
            configurations.forEach(config -> {
                if (config.getRedirectKey() != null) {
                    cache.put(config.getRedirectKey(), config);
                }
            });
        }
    }

    private Cache getCache() {
        return cacheManager.getCache(properties.getCacheName());
    }
}
