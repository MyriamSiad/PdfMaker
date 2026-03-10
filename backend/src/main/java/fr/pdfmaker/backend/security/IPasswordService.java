package fr.pdfmaker.backend.security;


/**
 *  Interface pour le service de gestion des mots de passe.
 * Fournit des méthodes pour le hachage et la vérification des mots de passe.
 *
 * @Author  : Myriam Siad
 *
 */
public interface IPasswordService {

    String hash ( String password ) throws Exception;
    boolean verify ( String password, String hash ) throws Exception;
}
