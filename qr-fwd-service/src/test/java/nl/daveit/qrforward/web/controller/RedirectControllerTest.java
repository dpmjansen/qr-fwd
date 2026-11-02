package nl.daveit.qrforward.web.controller;

import nl.daveit.qrforward.domain.api.RedirectApi;
import nl.daveit.qrforward.domain.model.RedirectConfiguration;
import nl.daveit.qrforward.domain.model.RedirectKey;
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
    private static final String REDIRECT_HEADER = "X-Redirect-UUID";

    @Autowired
    MockMvcTester mockMvc;

    @MockitoBean
    private RedirectApi redirectApi;

    @MockitoBean //todo check it this can be removed
    private CacheManager cacheManager;

    @Test
    void testRedirectWithValidUuid() {
        //given
        UUID reqId = UUID.randomUUID();

        RedirectConfiguration domainConfig = new RedirectConfiguration(new RedirectKey(reqId), "https://temp.example.com", 307, true, null);

        when(redirectApi.getRedirectConfiguration(any(RedirectKey.class))).thenReturn(Optional.of(domainConfig));

        //when / then
        assertThat(mockMvc.perform(MockMvcRequestBuilders.get(REDIRECT_ENDPOINT + "/" + reqId)))
                .hasStatus(HttpStatus.TEMPORARY_REDIRECT)
                .hasRedirectedUrl(domainConfig.getDestinationUrl())
                .hasHeader(REDIRECT_HEADER, domainConfig.getRedirectKey().key().toString());
    }

    @Test
    void testRedirectWithPermanentRedirect() {
        //given
        UUID reqId = UUID.randomUUID();

        RedirectConfiguration domainConfig = new RedirectConfiguration(new RedirectKey(reqId), "https://perm.example.com", 308, true, null);

        when(redirectApi.getRedirectConfiguration(any(RedirectKey.class))).thenReturn(Optional.of(domainConfig));

        //when / then
        assertThat(mockMvc.perform(MockMvcRequestBuilders.get(REDIRECT_ENDPOINT + "/" + reqId)))
                .hasStatus(HttpStatus.PERMANENT_REDIRECT)
                .hasRedirectedUrl(domainConfig.getDestinationUrl())
                .hasHeader(REDIRECT_HEADER, domainConfig.getRedirectKey().key().toString());
    }

}