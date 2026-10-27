package nl.daveit.qrforward;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main application class for QR Forward Service.
 * This service provides HTTP redirect functionality with configurable
 * destinations and supports both 307 (Temporary Redirect) and 308 (Permanent Redirect) status codes.
 * Only GET requests are supported, and redirects are based on UUID.
 */
@SpringBootApplication
@EnableCaching
public class QrForwardServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(QrForwardServiceApplication.class, args);
    }
}
