package nl.daveit.qrforward.domain.service;

import nl.daveit.qrforward.domain.api.RedirectApi;
import nl.daveit.qrforward.domain.config.RedirectServiceProperties;
import nl.daveit.qrforward.domain.spi.RedirectConfigurationProvider;
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
     * Loads all redirect configurations into the cache.
     */
    @Override
    public void loadAllConfigurationsIntoCache() {
        Cache cache = getCache();
        if (cache != null) {
            List<RedirectConfiguration> configurations = redirectConfigurationProvider.getAllRedirectConfigurations();
            configurations.forEach(config -> {
                if (config.getRedirectKey() != null) {
                    cache.put(config.getRedirectKey(), config);
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
        Optional<RedirectConfiguration> config = redirectConfigurationProvider.getById(redirectKey);
        
        // If found and caching is enabled, add to cache
        config.ifPresent(c -> {
            Cache cacheToUse = getCache();
            if (cacheToUse != null && c.getRedirectKey() != null) {
                cacheToUse.put(c.getRedirectKey(), c);
            }
        });

        return config;
    }

    private Cache getCache() {
        return cacheManager.getCache(properties.getCacheName());
    }
}
