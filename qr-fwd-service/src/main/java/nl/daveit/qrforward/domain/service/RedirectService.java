package nl.daveit.qrforward.domain.service;

import nl.daveit.qrforward.domain.api.RedirectApi;
import nl.daveit.qrforward.domain.config.RedirectServiceProperties;
import nl.daveit.qrforward.domain.spi.RedirectStore;
import nl.daveit.qrforward.domain.model.RedirectConfiguration;
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

    private final RedirectStore redirectStore;
    private final CacheManager cacheManager;
    private final RedirectServiceProperties properties;

    public RedirectService(
            RedirectStore redirectStore,
            CacheManager cacheManager,
            RedirectServiceProperties properties) {
        this.redirectStore = redirectStore;
        this.cacheManager = cacheManager;
        this.properties = properties;

        loadAllConfigurationsIntoCache();
    }

    /**
     * Loads all redirect configurations into the cache.
     */
    @Override
    public void loadAllConfigurationsIntoCache() {
        Cache cache = getCache();
        if (cache != null) {
            List<RedirectConfiguration> configurations = redirectStore.getAllRedirectConfigurations();
            configurations.forEach(config -> {
                if (config.getUuid() != null) {
                    cache.put(config.getUuid(), config);
                }
            });
        }
    }

    /**
     * Retrieves a redirect configuration by UUID.
     * First checks the cache, then the database.
     * 
     * @param redirectKey the UUID to match
     * @return Optional containing the matching configuration, or empty if not found
     */
    @Override
    public Optional<RedirectConfiguration> getRedirectConfiguration(RedirectKey redirectKey) {
        // Check cache first
        Cache cache = getCache();
        if (cache != null) {
            RedirectConfiguration config = cache.get(redirectKey.key(), RedirectConfiguration.class);
            if (config != null) {
                return Optional.of(config);
            }
        }

        // If not in cache or caching disabled, check database
        Optional<RedirectConfiguration> config = redirectStore.getById(redirectKey);
        
        // If found and caching is enabled, add to cache
        config.ifPresent(c -> {
            Cache cacheToUse = getCache();
            if (cacheToUse != null && c.getUuid() != null) {
                cacheToUse.put(c.getUuid(), c);
            }
        });

        return config;
    }

    /**
     * Gets all redirect configurations.
     * 
     * @return list of all configurations
     */
    @Override
    public List<RedirectConfiguration> getAllConfigurations() {
        return redirectStore.getAllRedirectConfigurations();
    }

    private Cache getCache() {
        return cacheManager.getCache(properties.getCacheName());
    }
}
