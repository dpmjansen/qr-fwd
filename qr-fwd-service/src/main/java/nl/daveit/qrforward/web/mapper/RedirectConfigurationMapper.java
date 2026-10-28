package nl.daveit.qrforward.web.mapper;

import nl.daveit.qrforward.domain.model.RedirectConfiguration;
import nl.daveit.qrforward.openapi.model.RedirectConfigurationResponse;
import org.springframework.stereotype.Component;

@Component
public class RedirectConfigurationMapper {

    public RedirectConfigurationResponse fromDomain(RedirectConfiguration domain) {

        var web = new RedirectConfigurationResponse();
        web.setUuid(domain.getRedirectKey().key());
        web.setDestinationUrl(domain.getDestinationUrl());
        web.setRedirectType(RedirectConfigurationResponse.RedirectTypeEnum.fromValue(domain.getRedirectType()));
        web.setEnabled(domain.getEnabled());
        web.setDescription(domain.getDescription());

        return web;
    }

}
