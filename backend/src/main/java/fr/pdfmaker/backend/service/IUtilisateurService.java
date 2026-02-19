package fr.pdfmaker.backend.service;

import fr.pdfmaker.backend.model.dto.LoginDto;
import fr.pdfmaker.backend.model.dto.UtilisateurCreationDto;
import fr.pdfmaker.backend.model.dto.UtilisateurDto;
import fr.pdfmaker.backend.model.entity.Utilisateur;
import org.springframework.stereotype.Service;


public interface IUtilisateurService {

    UtilisateurDto getUtilsateur (long id) throws Exception;

    /**
     * Methode qui permet de créer un compte utilisateur ou de le mettre à jour s'il existe déjà
     * @param user
     * @return
     * @throws Exception
     * @author Myriam S.
     */
    Long createUser(UtilisateurCreationDto user)  throws Exception;
    Long updateUser (UtilisateurDto user)  throws Exception;
    UtilisateurDto loginUser(LoginDto login) throws Exception;



}
