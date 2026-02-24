package fr.pdfmaker.backend.config;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories (basePackages = "fr.pdfmaker.backend.repository")
@EntityScan (basePackages = "fr.pdfmaker.backend.model.entity")
public class JpaConfig {
}
