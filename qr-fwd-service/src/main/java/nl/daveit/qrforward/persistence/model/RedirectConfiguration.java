package nl.daveit.qrforward.persistence.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * Entity representing a redirect configuration.
 * Uses UUID as the identifier to redirect to the destination URL.
 */
@Entity
@Table(name = "redirect_configurations")
public class RedirectConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    /**
     * The destination URL to redirect to.
     */
    @NotBlank(message = "Destination URL is required")
    @Column(nullable = false)
    private String destinationUrl;

    /**
     * The HTTP status code for the redirect.
     * 307 = Temporary Redirect
     * 308 = Permanent Redirect
     */
    @NotNull(message = "Redirect type is required")
    @Column(nullable = false)
    private Integer redirectType = 308;

    /**
     * Whether this configuration is enabled.
     */
    @NotNull(message = "Enabled status is required")
    @Column(nullable = false)
    private Boolean enabled = true;

    /**
     * Description of this redirect configuration.
     */
    @Column(length = 500)
    private String description;

    /**
     * Timestamp when this configuration was created.
     */
    @CreationTimestamp
    @Column(updatable = false, nullable = true)
    private Instant createdAt;

    /**
     * Timestamp when this configuration was last updated.
     */
    @UpdateTimestamp
    @Column(nullable = true)
    private Instant updatedAt;

    // Constructors

    public RedirectConfiguration() {
        this.uuid = UUID.randomUUID();
    }

    public RedirectConfiguration(UUID uuid, String destinationUrl,
                                 Integer redirectType, Boolean enabled,
                                 String description, Instant createdAt, Instant updatedAt) {
        this.uuid = uuid;
        this.destinationUrl = destinationUrl;
        this.redirectType = redirectType;
        this.enabled = enabled;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getDestinationUrl() {
        return destinationUrl;
    }

    public void setDestinationUrl(String destinationUrl) {
        this.destinationUrl = destinationUrl;
    }

    public Integer getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(Integer redirectType) {
        this.redirectType = redirectType;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
