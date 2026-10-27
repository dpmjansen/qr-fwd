package nl.daveit.qrforward.domain.service;

import nl.daveit.qrforward.domain.config.RedirectServiceProperties;
import nl.daveit.qrforward.domain.model.RedirectKey;
import nl.daveit.qrforward.domain.spi.RedirectStore;
import nl.daveit.qrforward.domain.model.RedirectConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedirectServiceTest {

    @Mock
    private RedirectStore redirectStore;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private RedirectServiceProperties properties;

    @InjectMocks
    private RedirectService redirectService;

    private Cache mockCache;
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
        config1 = new RedirectConfiguration(redirectKey1.key(), "https://temp.example.com", 307, true, null);
        config2 = new RedirectConfiguration(redirectKey2.key(), "https://perm.example.com", 308, true, null);

        mockCache = mock(Cache.class);
    }

    @Test
    void testRedirectServiceGetConfigurationByUuid() {
        //given
        when(properties.getCacheName()).thenReturn("foo");
        when(mockCache.get(redirectKey1.key(), RedirectConfiguration.class)).thenReturn(config1);
        when(cacheManager.getCache(anyString())).thenReturn(mockCache);

        //when
        Optional<RedirectConfiguration> config = redirectService.getRedirectConfiguration(redirectKey1);

        // Test retrieving a configuration by UUID
        assertThat(config).isPresent();
        assertThat(config.get().getUuid()).isEqualTo(redirectKey1.key());
        assertThat(config.get().getDestinationUrl()).isEqualTo("https://temp.example.com");
        assertThat(config.get().getRedirectType()).isEqualTo(307);
    }

    @Test
    void testRedirectServiceNoMatch() {
        // Test no match scenario
        RedirectKey redirectKey = new RedirectKey(UUID.randomUUID());
        Optional<RedirectConfiguration> config = redirectService.getRedirectConfiguration(redirectKey);

        assertThat(config).isEmpty();
    }
}