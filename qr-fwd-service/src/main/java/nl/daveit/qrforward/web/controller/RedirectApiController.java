package nl.daveit.qrforward.web.controller;

import nl.daveit.qrforward.domain.api.RedirectApi;
import nl.daveit.qrforward.domain.model.RedirectKey;
import nl.daveit.qrforward.domain.model.RedirectConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST API Controller for managing redirect configurations.
 * Handles all management endpoints under /api/redirect-configs.
 */
@RestController
@RequestMapping("/api/redirect-configs")
public class RedirectApiController {

    private final RedirectApi redirectApi;

    public RedirectApiController(RedirectApi redirectApi) {
        this.redirectApi = redirectApi;
    }

    /**
     * Gets all redirect configurations.
     */
    @GetMapping
    public ResponseEntity<List<RedirectConfiguration>> getAllConfigurations() {
        List<RedirectConfiguration> configurations = redirectApi.getAllConfigurations();
        return ResponseEntity.ok(configurations);
    }

    /**
     * Gets a specific redirect configuration by UUID.
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<RedirectConfiguration> getConfigurationByUuid(@PathVariable UUID uuid) {
        RedirectKey redirectKey = new RedirectKey(uuid);
        Optional<RedirectConfiguration> configuration = redirectApi.getRedirectConfiguration(redirectKey);
        return configuration.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
