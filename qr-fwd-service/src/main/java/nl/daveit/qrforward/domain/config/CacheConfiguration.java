package nl.daveit.qrforward.domain.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Cache configuration for the redirect service.
 * Uses Caffeine cache for in-memory caching of redirect configurations.
 */
@Configuration
@EnableCaching
public class CacheConfiguration {

    private final RedirectServiceProperties redirectServiceProperties;

    public CacheConfiguration(RedirectServiceProperties redirectServiceProperties) {
        this.redirectServiceProperties = redirectServiceProperties;
    }

    /**
     * Creates and configures the Caffeine cache manager.
     * @return configured CacheManager
     */
    @Bean
    @ConditionalOnProperty(prefix = "app.redirect", name = "cache-enabled", havingValue = "true", matchIfMissing = true)
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        
        // Configure the redirect configurations cache
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(redirectServiceProperties.getCacheTtlMs(), TimeUnit.MILLISECONDS)
                .maximumSize(1000)
                .recordStats());
        
        cacheManager.setAllowNullValues(false);
        
        return cacheManager;
    }

    /**
     * Creates a simple cache manager without Caffeine configuration.
     * Used when caching is disabled.
     * @return simple CacheManager
     */
    @Bean
    @ConditionalOnProperty(prefix = "app.redirect", name = "cache-enabled", havingValue = "false")
    public CacheManager simpleCacheManager() {
        return new CaffeineCacheManager();
    }
}
