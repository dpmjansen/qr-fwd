package nl.daveit.qrforward.web.controller;

import nl.daveit.qrforward.domain.api.RedirectApi;
import nl.daveit.qrforward.domain.model.RedirectKey;
import nl.daveit.qrforward.generated.web.model.RedirectConfiguration;
import nl.daveit.qrforward.web.mapper.RedirectConfigurationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller for handling HTTP redirects.
 * Only supports GET requests to /redirect/{uuid} endpoint.
 * Supports both 307 (Temporary Redirect) and 308 (Permanent Redirect) status codes.
 */
@RestController
public class RedirectController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedirectController.class);

    private final RedirectApi redirectApi;
    private final RedirectConfigurationMapper mapper;

    public RedirectController(RedirectApi redirectApi, RedirectConfigurationMapper mapper) {
        this.redirectApi = redirectApi;
        this.mapper = mapper;
    }

    /**
     * Handles GET requests to /redirect/{uuid} and performs redirection.
     * This is the ONLY endpoint that performs redirects.
     * Only GET method is supported.
     *
     * @param uuid the UUID string from the URL path
     */
    @GetMapping("/redirect/{uuid}")
    public ResponseEntity<Void> handleRedirect(@PathVariable UUID uuid) {
        
        // Parse UUID
        RedirectKey redirectKey;
        try {
            redirectKey = new RedirectKey(uuid);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid UUID format: " + uuid);
            return ResponseEntity.badRequest().build();
        }
        
        // Find matching configuration by UUID
        Optional<RedirectConfiguration> config = redirectApi.getRedirectConfiguration(redirectKey).map(mapper::fromDomain);
        
        if (config.isPresent() && config.get().getEnabled()) {
            RedirectConfiguration redirectConfig = config.get();
            String destinationUrl = redirectConfig.getDestinationUrl();
            RedirectConfiguration.RedirectTypeEnum redirectType = redirectConfig.getRedirectType();
            
            // Validate destination URL
            if (destinationUrl == null || destinationUrl.trim().isEmpty()) {
                LOGGER.error("Destination URL is not configured");
                return ResponseEntity.badRequest().build();
            }
            
            // Try to validate destination URL
            try {
                URI destinationUri = new URI(destinationUrl);
                return ResponseEntity
                        .status(redirectType.getValue())
                        .header("Location", destinationUri.toASCIIString())
                        .header("X-Redirect-UUID", redirectConfig.getUuid().toString())
                        .build();

            } catch (URISyntaxException e) {
                LOGGER.error("Invalid destination URL: " + destinationUrl);
                return ResponseEntity.badRequest().build();
            }
        } else {
            LOGGER.info("No redirect configuration found for UUID: " + uuid);
            return ResponseEntity.notFound().build();
        }
    }
}
