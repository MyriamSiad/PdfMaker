package fr.pdfmaker.backend.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration

/**
 * Configuration de sécurité pour l'application. Cette classe configure les règles de sécurité pour les requêtes HTTP.
 * Actuellement, elle désactive la protection CSRF et permet toutes les requêtes sans authentification. Cela peut être ajusté en fonction des besoins de sécurité de l'application.
 */
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())   // 👈 désactive CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
