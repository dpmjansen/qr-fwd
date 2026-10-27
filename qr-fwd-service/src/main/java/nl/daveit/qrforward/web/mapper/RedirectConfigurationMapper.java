package nl.daveit.qrforward.web.mapper;

import nl.daveit.qrforward.domain.model.RedirectConfiguration;
import org.springframework.stereotype.Component;

@Component
public class RedirectConfigurationMapper {

    public nl.daveit.qrforward.generated.web.model.RedirectConfiguration fromDomain(RedirectConfiguration domain) {

        var web = new nl.daveit.qrforward.generated.web.model.RedirectConfiguration();
        web.setUuid(domain.getUuid());
        web.setDestinationUrl(domain.getDestinationUrl());
        web.setRedirectType(nl.daveit.qrforward.generated.web.model.RedirectConfiguration.RedirectTypeEnum.fromValue(domain.getRedirectType()));
        web.setEnabled(domain.getEnabled());
        web.setDescription(domain.getDescription());

        return web;
    }

}
