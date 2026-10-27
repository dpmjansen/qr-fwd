package nl.daveit.qrforward.web.controller;

import nl.daveit.qrforward.domain.api.RedirectApi;
import nl.daveit.qrforward.domain.model.RedirectKey;
import nl.daveit.qrforward.domain.model.RedirectConfiguration;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebMvcTest(RedirectApiController.class)
class RedirectApiControllerTest {

    private static final String REDIRECT_API_ENDPOINT = "/api/redirect-configs";

    @Autowired
    MockMvcTester mockMvc;

    @MockitoBean
    private CacheManager cacheManager;

    @MockitoBean
    private RedirectApi redirectApi;

    private RedirectKey redirectKey1;
    private RedirectKey redirectKey2;
    private RedirectConfiguration config1;
    private RedirectConfiguration config2;

    @BeforeEach
    void setUp() {
        // Generate test UUIDs
        redirectKey1 = new RedirectKey(UUID.randomUUID());
        redirectKey2 = new RedirectKey(UUID.randomUUID());

        // Insert test data
        config1 = new RedirectConfiguration(redirectKey1, "https://temp.example.com", 307, true, null);
        config2 = new RedirectConfiguration(redirectKey2, "https://perm.example.com", 308, true, null);
    }

    @Test
    void testManagementApiGetAll() {
        //given
        when(redirectApi.getAllConfigurations()).thenReturn(List.of(config1, config2));

        //when / then
        assertThat(mockMvc.perform(MockMvcRequestBuilders.get(REDIRECT_API_ENDPOINT)))
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .convertTo(InstanceOfAssertFactories.list(RedirectConfiguration.class))
                .hasSize(2)
                .extracting(RedirectConfiguration::getRedirectKey)
                .contains(redirectKey1, redirectKey2);
    }

    @Test
    void testManagementApiGetByUuid() {
        //given
        when(redirectApi.getRedirectConfiguration(redirectKey2)).thenReturn(Optional.ofNullable(config2));

        //when / then
        assertThat(mockMvc.perform(MockMvcRequestBuilders.get(REDIRECT_API_ENDPOINT + "/" + redirectKey2.key().toString())))
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .convertTo(RedirectConfiguration.class)
                .extracting(RedirectConfiguration::getRedirectKey)
                .isEqualTo(config2.getRedirectKey());
    }
}