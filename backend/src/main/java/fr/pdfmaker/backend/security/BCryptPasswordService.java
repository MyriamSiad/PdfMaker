package fr.pdfmaker.backend.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *  Service de gestion des mots de passe utilisant l'algorithme BCrypt.
 * Fournit des méthodes pour le hachage et la vérification des mots de passe.
 *
 * @Author  : Myriam Siad
 * */


public class BCryptPasswordService implements IPasswordService{

    /**
     * Le facteur de travail (strength) pour le hachage BCrypt. Plus il est élevé, plus le hachage est sécurisé, mais aussi plus lent.
     * Une valeur courante est 10, mais vous pouvez l'ajuster en fonction de vos besoins de sécurité et de performance.
     */
    private final BCryptPasswordEncoder encoder;
    public BCryptPasswordService(int strength) {

        this.encoder = new BCryptPasswordEncoder(strength); // Utilisez une implémentation de BCryptPasswordEncoder
    }

    /**
     * Hache le mot de passe en utilisant BCrypt.
     * @param password : le mot de passe en clair à hacher
     * @return le hachage du mot de passe
     * @throws Exception
     *
     * @Author : Myriam Siad
     */
    @Override
    public String hash(String password) throws Exception {
        return encoder.encode(password);
    }


    /**
     * Vérifie si le mot de passe correspond au hachage BCrypt.
     * @param password : le mot de passe en clair à vérifier
     * @param hash  : le hachage du mot de passe à comparer
     * @return boolean indiquant si le mot de passe correspond au hachage
     * @throws Exception
     */
    @Override
    public boolean verify(String password, String hash) throws Exception {
        return encoder.matches(password , hash);
    }
}
