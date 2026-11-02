package nl.daveit.qrforward.web.controller;

import nl.daveit.qrforward.domain.api.RedirectApi;
import nl.daveit.qrforward.domain.model.SimpleRedirectConfiguration;
import nl.daveit.qrforward.domain.model.RedirectKey;
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

    public RedirectController(RedirectApi redirectApi) {
        this.redirectApi = redirectApi;
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
        RedirectKey redirectKey = new RedirectKey(uuid);

        // Find matching configuration by UUID
        Optional<SimpleRedirectConfiguration> config = redirectApi.getRedirectConfiguration(redirectKey);
        
        if (config.isPresent()) {
            SimpleRedirectConfiguration redirectConfig = config.get();
            String destinationUrl = redirectConfig.getDestinationUrl();
            Integer redirectType = redirectConfig.getRedirectType();
            
            // Try to validate destination URL
            try {
                URI destinationUri = new URI(destinationUrl);
                return ResponseEntity
                        .status(redirectType)
                        .header("Location", destinationUri.toASCIIString())
                        .header("X-Redirect-UUID", redirectConfig.getRedirectKey().key().toString())
                        .build();

            } catch (URISyntaxException e) {
                LOGGER.error("Invalid destination URL: " + destinationUrl);
                return ResponseEntity.internalServerError().build();
            }
        } else {
            LOGGER.info("No active redirect configuration found for UUID: " + uuid);
            return ResponseEntity.notFound().build();
        }
    }
}
