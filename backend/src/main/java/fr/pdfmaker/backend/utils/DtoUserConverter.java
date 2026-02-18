package fr.pdfmaker.backend.utils;

import fr.pdfmaker.backend.model.dto.UtilisateurCreationDto;
import fr.pdfmaker.backend.model.entity.Utilisateur;

public final  class DtoUserConverter {

    /**
     * C'est une methode qui permet de convertir un UtilisateurCreationDto, en simple utilisateur ! Pour pouvoir l'utiliser dans la BDD
     * @param userDto Le dto qui se charge de créer un user.
     * @return  un objet user.
     */
    public static  Utilisateur  concertUserDtoToUser(UtilisateurCreationDto userDto){
        Utilisateur user = new Utilisateur();

        user.setPrenom(userDto.getPrenom());
        user.setPasswordHash(userDto.getPasswordHash());
        user.setNom(userDto.getNom());
        user.setEmail(userDto.getEmail());
        user.setDateCreationCompte(null);  //Faut récuperer le timestamp

        return user;
    }
}
