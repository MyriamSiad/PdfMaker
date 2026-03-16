package fr.pdfmaker.backend.utils;

import fr.pdfmaker.backend.model.dto.utilisateur.InscriptionRequestDto;
import fr.pdfmaker.backend.model.dto.utilisateur.UtilisateurDto;
import fr.pdfmaker.backend.model.entity.Utilisateur;

public final  class DtoUserConverter {

    /**
     * C'est une methode qui permet de convertir un InscriptionRequestDto, en simple utilisateur ! Pour pouvoir l'utiliser dans la BDD
     * @param userDto Le dto qui se charge de créer un user.
     * @return  un objet user.
     */
    public static  Utilisateur  convertUserDtoToUser(InscriptionRequestDto userDto){
        Utilisateur user = new Utilisateur();

        user.setPrenom(userDto.getPrenom());
        user.setPasswordHash(userDto.getPasswordHash());
        user.setNom(userDto.getNom());
        user.setEmail(userDto.getEmail());
        return user;
    }

    /**
     * C'est une methode qui permet de convertir un Utilisateur, en simple UtilisateurDto ! Pour pouvoir l'utiliser dans les réponses de l'API
     * @param user Le user qu'on veut convertir en dto.
     * @return  un objet userDto.
     */
    public static UtilisateurDto convertUserToUserDto (Utilisateur user){
        UtilisateurDto userDto = new UtilisateurDto();

        userDto.setPrenom(user.getPrenom());
        userDto.setNom(user.getNom());
        userDto.setEmail(user.getEmail());
        userDto.setIdUser(user.getIdUser());

        return userDto;
    }
}
