package fr.pdfmaker.backend.service;

import fr.pdfmaker.backend.model.dto.LoginDto;
import fr.pdfmaker.backend.model.dto.UtilisateurCreationDto;
import fr.pdfmaker.backend.model.dto.UtilisateurDto;
import fr.pdfmaker.backend.model.entity.Utilisateur;
import org.springframework.stereotype.Service;


public interface IUtilisateurService {

    UtilisateurDto getUtilsateur (long id) throws Exception;
    Long addOrUpdateUser (UtilisateurCreationDto user) throws Exception;
    UtilisateurDto loginUser(LoginDto login) throws Exception;



}
