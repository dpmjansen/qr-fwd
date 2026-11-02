package nl.daveit.qrforward.domain.model;

public sealed interface RedirectConfiguration permits ScheduledRedirectConfiguration, SimpleRedirectConfiguration {

    RedirectKey getRedirectKey();

    String getDestinationUrl();

    Integer getRedirectType();

}
