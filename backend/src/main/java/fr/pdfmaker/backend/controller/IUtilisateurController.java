package fr.pdfmaker.backend.controller;

import fr.pdfmaker.backend.model.dto.LoginDto;
import fr.pdfmaker.backend.model.dto.UtilisateurCreationDto;
import fr.pdfmaker.backend.model.dto.UtilisateurDto;
import fr.pdfmaker.backend.model.entity.Utilisateur;
import org.springframework.http.ResponseEntity;

/**
 * Interface pour le controller de l'utilisateur
 * Permet de définir les méthodes qui seront utilisées pour gérer les utilisateurs
 * et de les implémenter dans la classe UtilisateurController.
 */
public interface IUtilisateurController {

    String getInfos();

    ResponseEntity<Long> createUtilisateur(UtilisateurCreationDto user );
    ResponseEntity<Long> updateUtilisateur(UtilisateurDto user);

    ResponseEntity<UtilisateurDto> connexionUtilisateur(LoginDto login);

}
