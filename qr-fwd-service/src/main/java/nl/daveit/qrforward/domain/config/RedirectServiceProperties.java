package nl.daveit.qrforward.domain.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for the redirect service.
 * These properties can be configured via application.properties or application.yml.
 */
@Component
@ConfigurationProperties(prefix = "app.redirect")
public class RedirectServiceProperties {

    /**
     * Cache name for redirect configurations.
     */
    private String cacheName = "redirectConfigurations";

    /**
     * Cache TTL (time to live) in milliseconds.
     */
    private long cacheTtlMs = 600000; // 10 minutes

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public long getCacheTtlMs() {
        return cacheTtlMs;
    }

    public void setCacheTtlMs(long cacheTtlMs) {
        this.cacheTtlMs = cacheTtlMs;
    }

}
