package nl.daveit.qrforward.web.controller;

import nl.daveit.qrforward.domain.api.RedirectApi;
import nl.daveit.qrforward.domain.model.RedirectKey;
import nl.daveit.qrforward.domain.model.RedirectConfiguration;
import nl.daveit.qrforward.web.mapper.RedirectConfigurationMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(RedirectController.class)
class RedirectControllerTest {

    private static final String REDIRECT_ENDPOINT = "/redirect";

    @Autowired
    MockMvcTester mockMvc;

    @MockitoBean
    private RedirectApi redirectApi;

    @MockitoBean
    private RedirectConfigurationMapper mapper;

    @MockitoBean //todo check it this can be removed
    private CacheManager cacheManager;

    @Test
    void testRedirectWithValidUuid() {
        //given
        UUID reqId = UUID.randomUUID();

        RedirectConfiguration domainConfig = new RedirectConfiguration(reqId, "https://temp.example.com", 307, true, null);
        nl.daveit.qrforward.generated.web.model.RedirectConfiguration web = new nl.daveit.qrforward.generated.web.model.RedirectConfiguration();
        web.setUuid(reqId);
        web.setDestinationUrl(domainConfig.getDestinationUrl());
        web.setRedirectType(nl.daveit.qrforward.generated.web.model.RedirectConfiguration.RedirectTypeEnum.fromValue(domainConfig.getRedirectType()));
        web.setEnabled(true);

        when(redirectApi.getRedirectConfiguration(any(RedirectKey.class))).thenReturn(Optional.of(domainConfig));
        when(mapper.fromDomain(domainConfig)).thenReturn(web);

        //when / then
        assertThat(mockMvc.perform(MockMvcRequestBuilders.get(REDIRECT_ENDPOINT + "/" + reqId)))
                .hasStatus(HttpStatus.TEMPORARY_REDIRECT)
                .hasRedirectedUrl(domainConfig.getDestinationUrl());
    }

    @Test
    void testRedirectWithPermanentRedirect() {
        //given
        UUID reqId = UUID.randomUUID();

        RedirectConfiguration domainConfig = new RedirectConfiguration(reqId, "https://perm.example.com", 308, true, null);
        nl.daveit.qrforward.generated.web.model.RedirectConfiguration web = new nl.daveit.qrforward.generated.web.model.RedirectConfiguration();
        web.setUuid(reqId);
        web.setDestinationUrl(domainConfig.getDestinationUrl());
        web.setRedirectType(nl.daveit.qrforward.generated.web.model.RedirectConfiguration.RedirectTypeEnum.fromValue(domainConfig.getRedirectType()));
        web.setEnabled(true);

        when(redirectApi.getRedirectConfiguration(any(RedirectKey.class))).thenReturn(Optional.of(domainConfig));
        when(mapper.fromDomain(domainConfig)).thenReturn(web);

        //when / then
        assertThat(mockMvc.perform(MockMvcRequestBuilders.get(REDIRECT_ENDPOINT + "/" + reqId)))
                .hasStatus(HttpStatus.PERMANENT_REDIRECT)
                .hasRedirectedUrl(domainConfig.getDestinationUrl());
    }

}