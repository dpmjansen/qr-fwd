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
     * Default redirect status code to use when not specified in configuration.
     * Valid values: 307 or 308
     */
    private int defaultStatusCode = 308;

    /**
     * Cache name for redirect configurations.
     */
    private String cacheName = "redirectConfigurations";

    /**
     * Cache TTL (time to live) in milliseconds.
     */
    private long cacheTtlMs = 600000; // 10 minutes

    // Getters and Setters

    public int getDefaultStatusCode() {
        return defaultStatusCode;
    }

    public void setDefaultStatusCode(int defaultStatusCode) {
        this.defaultStatusCode = defaultStatusCode;
    }

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
