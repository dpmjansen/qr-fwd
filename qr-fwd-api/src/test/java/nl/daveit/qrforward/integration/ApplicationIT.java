package nl.daveit.qrforward.integration;

import nl.daveit.qrforward.domain.model.RedirectConfiguration;
import nl.daveit.qrforward.domain.model.RedirectKey;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.RedirectConfigurationResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Profile("integration-test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationIT {

    private static final String ENDPOINT_API_REDIRECT_CONFIGS = "/api/redirect/configurations";
    private static final String ENDPOINT_API_REDIRECT = "/api/redirect";

    private RestTestClient client;

    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer(
            "postgres:18-alpine"
    ).withInitScript("database/init.sql");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void beforeEach(){
        client = RestTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void nonExistingConfig() {
        client.get()
                .uri(ENDPOINT_API_REDIRECT + "/" + UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void existingTempRedirect() {
        var body = client.get()
                .uri(ENDPOINT_API_REDIRECT + "/126147c0-05a9-4a83-8b59-f3c8763a85c0")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<RedirectConfigurationResponse>() {})
                .returnResult()
                .getResponseBody();

        assertThat(body)
                .extracting(RedirectConfigurationResponse::getUuid)
                .isEqualTo(UUID.fromString("126147c0-05a9-4a83-8b59-f3c8763a85c0"));
    }

    @Test
    void existingPermanentRedirect() {
        var body = client.get()
                .uri(ENDPOINT_API_REDIRECT + "/6f372f07-f8c3-47f7-90f4-f5c3e980f23f")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<RedirectConfigurationResponse>() {})
                .returnResult()
                .getResponseBody();

        assertThat(body)
                .extracting(RedirectConfigurationResponse::getUuid)
                .isEqualTo(UUID.fromString("6f372f07-f8c3-47f7-90f4-f5c3e980f23f"));
    }

    @Test
    void allConfigs() {
        List<RedirectConfigurationResponse> configs = client.get()
                .uri(ENDPOINT_API_REDIRECT_CONFIGS)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<RedirectConfigurationResponse>>() {})
                .returnResult()
                .getResponseBody();

        assertThat(configs)
                .extracting(RedirectConfigurationResponse::getUuid)
                .contains(UUID.fromString("126147c0-05a9-4a83-8b59-f3c8763a85c0"),UUID.fromString("6f372f07-f8c3-47f7-90f4-f5c3e980f23f"));
    }

}
