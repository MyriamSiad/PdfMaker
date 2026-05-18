package fr.pdfmaker.backend.security;

import lombok.Getter;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 *  Service de gestion des mots de passe utilisant l'algorithme Argon2.
 * Fournit des méthodes pour le hachage et la vérification des mots de passe.
 *
 * @Author  : Myriam Siad
 * */

@Service
@Getter
public class Argon2PasswordService implements IPasswordService{

    private final  Argon2PasswordEncoder encoder ;

    public Argon2PasswordService() {
        // Initialisation de la configuration d'Argon2 si nécessaire
        this.encoder = new Argon2PasswordEncoder( 16 , 32 , 1 , 65536 , 3); // Utilisez une implémentation d'Argon2PasswordEncoder
    }

    /**
     * Hache le mot de passe en utilisant Argon2.
     * @param password
     * @return
     * @throws Exception
     *
     * @Author : Myriam Siad
     */
    @Override
    public String hash(String password) throws Exception {

        return encoder.encode(password);
    }

    @Override
    public boolean verify(String password, String hash) throws Exception {

        return encoder.matches(password, hash);
    }

}
