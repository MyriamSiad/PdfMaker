package fr.pdfmaker.backend.controller;

import fr.pdfmaker.backend.model.dto.LoginDto;
import fr.pdfmaker.backend.model.dto.InscriptionRequestDto;
import fr.pdfmaker.backend.model.dto.UtilisateurDto;
import org.springframework.http.ResponseEntity;

/**
 * Interface pour le controller de l'utilisateur
 * Permet de définir les méthodes qui seront utilisées pour gérer les utilisateurs
 * et de les implémenter dans la classe UtilisateurController.
 */
public interface IUtilisateurController {

    String getInfos();

    /**
     * Crée un utilisateur à partir d'un DTO de création d'utilisateur.
     * @param user Le DTO contenant les informations de l'utilisateur à créer.
     * @return L'ID de l'utilisateur créé.
     */
    ResponseEntity<Long> createUtilisateur(InscriptionRequestDto user );

    /**
     * Met à jour les informations d'un utilisateur à partir d'un DTO d'utilisateur.
     * @param user Le DTO contenant les informations de l'utilisateur à mettre à jour.
     * @return L'ID de l'utilisateur mis à jour.
     */
    ResponseEntity<Long> updateUtilisateur(Long idIdUser, UtilisateurDto user);

    /**
     * Authentifie un utilisateur à partir d'un DTO de connexion.
     * @param login Le DTO contenant les informations de connexion de l'utilisateur. (Login, password)
     * @return Un DTO de l'utilisateur authentifié.
     */
    ResponseEntity<UtilisateurDto> connexionUtilisateur(LoginDto login);

}
