package fr.pdfmaker.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

/**
 *  Classe de configuration pour les services de gestion des mots de passe.
 * Permet de centraliser la configuration des algorithmes de hachage utilisés pour les mots de passe.
 *
 * @Author  : Myriam Siad
 * */

@Configuration
public class PasswordConfig {

    public IPasswordService passwordService;

    @Bean
    public IPasswordService passwordService() {
       // this.passwordService = new BCryptPasswordService(12); // Utilisez une implémentation de BCryptPasswordService
       return  new Argon2PasswordService(); // Utilisez une implémentation d'Argon2PasswordService
    }

}
