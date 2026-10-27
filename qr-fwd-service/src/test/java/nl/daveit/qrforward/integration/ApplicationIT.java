package nl.daveit.qrforward.integration;

import nl.daveit.qrforward.domain.model.RedirectConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    private static final String REDIRECT_ENDPOINT = "/redirect";
    private static final String API_REDIRECT_CONFIGS_ENDPOINT = "/api/redirect-configs";

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
                .uri(REDIRECT_ENDPOINT + "/" + UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void existingTempRedirect() {
        client.get()
                .uri(REDIRECT_ENDPOINT + "/126147c0-05a9-4a83-8b59-f3c8763a85c0")
                .exchange()
                .expectStatus().isTemporaryRedirect();
    }

    @Test
    void existingPermanentRedirect() {
        client.get()
                .uri(REDIRECT_ENDPOINT + "/6f372f07-f8c3-47f7-90f4-f5c3e980f23f")
                .exchange()
                .expectStatus().isPermanentRedirect();
    }

    @Test
    void allConfigs() {
        List<RedirectConfiguration> configs = client.get()
                .uri(API_REDIRECT_CONFIGS_ENDPOINT)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<RedirectConfiguration>>() {})
                .returnResult()
                .getResponseBody();

        assertThat(configs)
                .extracting(RedirectConfiguration::getUuid)
                .contains(UUID.fromString("126147c0-05a9-4a83-8b59-f3c8763a85c0"),UUID.fromString("6f372f07-f8c3-47f7-90f4-f5c3e980f23f"));
    }

}
