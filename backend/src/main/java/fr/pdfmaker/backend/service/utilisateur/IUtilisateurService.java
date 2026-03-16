package fr.pdfmaker.backend.service.utilisateur;

import fr.pdfmaker.backend.model.dto.utilisateur.InscriptionRequestDto;
import fr.pdfmaker.backend.model.dto.utilisateur.LoginDto;
import fr.pdfmaker.backend.model.dto.utilisateur.UtilisateurDto;


public interface IUtilisateurService {

    UtilisateurDto getUtilsateur (long id) throws Exception;

    /**
     * Crée un utilisateur à partir d'un DTO de création d'utilisateur.
     * @param user Le DTO contenant les informations de l'utilisateur à créer.
     * @return L'ID de l'utilisateur créé.
     * @throws Exception Si une erreur survient lors de la création de l'utilisateur.
     */
    Long createUser(InscriptionRequestDto user)  throws Exception;

    /**
     * Met à jour les informations d'un utilisateur à partir d'un DTO d'utilisateur.
     * @param user Le DTO contenant les informations de l'utilisateur à mettre à jour.
     * @return L'ID de l'utilisateur mis à jour.
     * @throws Exception Si une erreur survient lors de la mise à jour de l'utilisateur.
     */
    Long updateUser (UtilisateurDto user)  throws Exception;

    /**
     * Authentifie un utilisateur à partir d'un DTO de connexion.
     * @param login Le DTO contenant les informations de connexion de l'utilisateur. (Login, password)
     * @return Un DTO de l'utilisateur authentifié.
     * @throws Exception Si une erreur survient lors de l'authentification de l'utilisateur.
     */
    UtilisateurDto loginUser(LoginDto login) throws Exception;



}
