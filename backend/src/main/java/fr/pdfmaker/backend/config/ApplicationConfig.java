package fr.pdfmaker.backend.config;
import fr.pdfmaker.backend.repository.IUtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final IUtilisateurRepository userRepository;

    // ──────────────────────────────────────────────
    // UserDetailsService — charge l'utilisateur depuis la BDD
    // ──────────────────────────────────────────────
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.getUtilisateurByEmail(username);
    }

    // ──────────────────────────────────────────────
    // AuthenticationProvider — c'est lui qui manquait !
    // ──────────────────────────────────────────────
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // ──────────────────────────────────────────────
    // AuthenticationManager — utile pour le login
    // ──────────────────────────────────────────────
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ──────────────────────────────────────────────
    // PasswordEncoder
    // ──────────────────────────────────────────────
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}