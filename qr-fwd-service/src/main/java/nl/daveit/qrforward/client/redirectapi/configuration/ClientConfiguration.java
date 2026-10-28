package nl.daveit.qrforward.client.redirectapi.configuration;

import nl.daveit.qrforward.client.redirectapi.service.RedirectApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfiguration {

    @Bean
    public RedirectApiService redirectApiService(HttpServiceProxyFactory factory) {
        return factory.createClient(RedirectApiService.class);
    }

    @Bean
    public HttpServiceProxyFactory proxyFactory(@Value("${qrfwd.api.baseUrl}") String uri) {
        RestClient client = RestClient.create(uri);
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(client)).build();
    }

}
