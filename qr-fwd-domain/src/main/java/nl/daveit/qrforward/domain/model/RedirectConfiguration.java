package nl.daveit.qrforward.domain.model;

import java.util.UUID;

public final class RedirectConfiguration {
    private UUID uuid;
    private String destinationUrl;
    private Integer redirectType;
    private Boolean enabled;
    private String description;

    public RedirectConfiguration(){}

    public RedirectConfiguration(UUID uuid, String destinationUrl, Integer redirectType, Boolean enabled, String description) {
        this.uuid = uuid;
        this.destinationUrl = destinationUrl;
        this.redirectType = redirectType;
        this.enabled = enabled;
        this.description = description;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getDestinationUrl() {
        return destinationUrl;
    }

    public Integer getRedirectType() {
        return redirectType;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setDestinationUrl(String destinationUrl) {
        this.destinationUrl = destinationUrl;
    }

    public void setRedirectType(Integer redirectType) {
        this.redirectType = redirectType;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
