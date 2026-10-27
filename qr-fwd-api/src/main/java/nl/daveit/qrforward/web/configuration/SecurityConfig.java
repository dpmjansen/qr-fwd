package nl.daveit.qrforward.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req.requestMatchers("/api/admin").hasRole("ROLE_ADMIN").anyRequest().authenticated())
                .authorizeHttpRequests(req -> req.requestMatchers("/api/redirect").hasAnyRole("ROLE_ADMIN", "ROLE_USER").anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .build();
    }



}
