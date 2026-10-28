package nl.daveit.qrforward.client.redirectapi.service;

import nl.daveit.qrforward.openapi.model.RedirectConfigurationResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;
import java.util.UUID;

public interface RedirectApiService {

    @GetExchange("/api/redirect/configurations/{uuid}")
    RedirectConfigurationResponse getConfiguration(@PathVariable UUID uuid);

    @GetExchange("/api/redirect/configurations")
    List<RedirectConfigurationResponse> getAllConfigurations();

}
