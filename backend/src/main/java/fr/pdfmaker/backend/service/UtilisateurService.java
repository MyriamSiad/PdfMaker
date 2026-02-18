package fr.pdfmaker.backend.service;

import fr.pdfmaker.backend.model.dto.LoginDto;
import fr.pdfmaker.backend.model.dto.UtilisateurCreationDto;
import fr.pdfmaker.backend.model.dto.UtilisateurDto;
import fr.pdfmaker.backend.model.entity.Utilisateur;
import fr.pdfmaker.backend.repository.IUtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static fr.pdfmaker.backend.utils.DtoUserConverter.concertUserDtoToUser;

@Service
public class UtilisateurService implements IUtilisateurService {

    @Autowired
    private IUtilisateurRepository utilisateurRepository;


    @Override
    public UtilisateurDto getUtilsateur(long id) throws Exception {
        return null;
    }

    /**
     * Methode qui permet de verfier si l'objet donné en paramètre est bien valide.
     * S'il contient bien toutes les infos de l'utilisateur pour la création de son compte
     * @param user c'est l'utilisateur (objet) qu'on veut vérifier,
     * @author Myriam S.
     */
    private void verifyUserInfo(UtilisateurCreationDto user){

        if (user == null ||
         user.getNom() == null || user.getEmail()  == null ||user.getPrenom() == null || user.getPasswordHash() == null){
         throw new IllegalArgumentException("Les données fournis sont invalide");
        }
    }

    /**
     *
     * @param email C'est l'email qu'on veut vérifier.
     * @return elle retourne true si il existe déjà un utilisateur avec cette email dans la bdd , false le cas inverse.
     * @author Myriam S.
     */
    private Boolean verifyExistingUserEmail (String email) {
        boolean flag = true;
        if(email.isBlank()){
            throw new IllegalArgumentException("Email vide !! ");

        }
        if(utilisateurRepository.getUtilisateurByEmail(email.trim()) != null){
            flag = false;
        }
        return flag;
    }

    @Override
    @Transactional
    public Long addOrUpdateUser(UtilisateurCreationDto user) throws Exception {
        if(user.getIdUser() == null ){ //Create
           boolean emailExists;
           emailExists = verifyExistingUserEmail(user.getEmail());
           if(!emailExists){
               throw new IllegalArgumentException( "Adresse mail déjà dans la BDD ! ");
           }

            verifyUserInfo(user);
            Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
            String passwordHash = encoder.encode(user.getPasswordHash());
            user.setPasswordHash(passwordHash);
            Utilisateur _user = concertUserDtoToUser(user);
            return utilisateurRepository.save(_user).getIdUser();
        }
        else{ //Update
            if( !utilisateurRepository.existsById(user.getIdUser())){
                throw new IllegalArgumentException("Cette utilisateur n'existe pas !  ");
            }
            Utilisateur userToUpdate = utilisateurRepository.findByIdUser(user.getIdUser());
            if(userToUpdate == null){
                throw new IllegalArgumentException("L'utilisateur n'a pas été retrouvé !");
            }
            userToUpdate.setNom(user.getNom());
            userToUpdate.setPrenom(user.getPrenom());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setPasswordHash(user.getPasswordHash());

            utilisateurRepository.save(userToUpdate);
            return user.getIdUser();
        }
    }

    @Override
    public UtilisateurDto loginUser(LoginDto login) throws Exception {
        return null;
    }
}
