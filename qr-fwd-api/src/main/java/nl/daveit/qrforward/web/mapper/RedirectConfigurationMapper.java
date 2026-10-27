package nl.daveit.qrforward.web.mapper;

import nl.daveit.qrforward.domain.model.RedirectConfiguration;
import nl.daveit.qrforward.domain.model.RedirectKey;
import org.openapitools.model.RedirectConfigurationRequest;
import org.openapitools.model.RedirectConfigurationResponse;
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

    public RedirectConfiguration toDomain(RedirectConfigurationRequest request) {
        var domain = new RedirectConfiguration();
        domain.setRedirectKey(new RedirectKey(request.getUuid()));
        domain.setDestinationUrl(request.getDestinationUrl());
        domain.setRedirectType(request.getRedirectType().getValue());
        domain.setEnabled(request.getEnabled());
        domain.setDescription(request.getDescription());

        return domain;
    }

}
