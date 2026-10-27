package nl.daveit.qrforward.domain.model;

public final class RedirectConfiguration {
    private RedirectKey redirectKey;
    private String destinationUrl;
    private Integer redirectType;
    private Boolean enabled;
    private String description;

    public RedirectConfiguration(){}

    public RedirectConfiguration(RedirectKey redirectKey, String destinationUrl, Integer redirectType, Boolean enabled, String description) {
        this.redirectKey = redirectKey;
        this.destinationUrl = destinationUrl;
        this.redirectType = redirectType;
        this.enabled = enabled;
        this.description = description;
    }

    public RedirectKey getRedirectKey() {
        return redirectKey;
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

    public void setRedirectKey(RedirectKey redirectKey) {
        this.redirectKey = redirectKey;
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
