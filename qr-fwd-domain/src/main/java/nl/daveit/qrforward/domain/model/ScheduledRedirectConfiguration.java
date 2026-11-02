package nl.daveit.qrforward.domain.model;

public final class ScheduledRedirectConfiguration implements RedirectConfiguration {

    private RedirectKey redirectKey;
    private String destinationUrl;
    private Integer redirectType;
    //start time, end time, destination

    @Override
    public RedirectKey getRedirectKey() {
        return redirectKey;
    }

    @Override
    public String getDestinationUrl() {
        return destinationUrl;
    }

    @Override
    public Integer getRedirectType() {
        return redirectType;
    }

    private RedirectScheduleEntry getCurrentSchedule(){

    }
}
