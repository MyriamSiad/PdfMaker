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
    private void verifyExistingUserEmail (String email , Long idUserToExclude) {

        if(email.isBlank()){
             throw new IllegalArgumentException("Email vide !! ");
         }
         Utilisateur user = utilisateurRepository.getUtilisateurByEmail(email.trim());
         if(user != null && !user.getIdUser().equals(idUserToExclude)){
             throw new IllegalArgumentException("Adresse mail déjà dans la BDD ! ");
         }
    }

    /**
     *
     * @param email C'est l'email qu'on veut vérifier.
     * @return elle retourne true si il existe déjà un utilisateur avec cette email dans la bdd , false le cas inverse.
     * @author Myriam S.
     */
    private void verifyExistingUserEmail (String email ) {
        if(email.isBlank()){
            throw new IllegalArgumentException("Email vide !! ");

        }
        Utilisateur user = utilisateurRepository.getUtilisateurByEmail(email.trim());
        if(user != null){
            throw new IllegalArgumentException("Adresse mail déjà dans la BDD ! ");
        }
    }


    /**
     * Methode qui permet de hasher le mot de passe de l'utilisateur avant de le stocker dans la bdd
     * @param password
     * @return le mot de passe hashé
     * @author Myriam S.
     */

    private String hashPassword(String password){
        Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        return encoder.encode(password);
    }

    /**
     * Methode qui permet de vérifier si le mot de passe donné par l'utilisateur lors de sa connexion correspond bien au mot de passe hashé stocké dans la bdd
     * @param rawPassword c'est le mot de passe donné par l'utilisateur lors de sa connexion
     * @param encodedPassword c'est le mot de passe hashé stocké dans la bdd
     * @return true si les deux mots de passe correspondent, false le cas inverse
     * @author Myriam S.
     */
    private boolean verifyPassword(String rawPassword, String encodedPassword){
        Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        return encoder.matches(rawPassword, encodedPassword);
    }


    @Override
    @Transactional
    public Long createUser(UtilisateurCreationDto user) throws Exception {
            boolean emailExists;
            verifyExistingUserEmail(user.getEmail());
            verifyUserInfo(user);
            String passwordHash = hashPassword(user.getPasswordHash());
            user.setPasswordHash(passwordHash);
            Utilisateur _user = concertUserDtoToUser(user);
            return utilisateurRepository.save(_user).getIdUser();
    }

    @Override
    @Transactional
    public Long updateUser (UtilisateurDto user) throws Exception {

            if( !utilisateurRepository.existsById(user.getIdUser())){
                throw new IllegalArgumentException("Cette utilisateur n'existe pas !  ");
            }
            Utilisateur userToUpdate = utilisateurRepository.findByIdUser(user.getIdUser());
            if(userToUpdate == null){
                throw new IllegalArgumentException("L'utilisateur n'a pas été retrouvé !");
            }
            userToUpdate.setNom(user.getNom());
            userToUpdate.setPrenom(user.getPrenom());
            verifyExistingUserEmail(user.getEmail(), user.getIdUser());
            userToUpdate.setEmail(user.getEmail());
            //userToUpdate.setPasswordHash(hashPassword(user.getPasswordHash()));
           //utilisateurRepository.save(userToUpdate);
            return user.getIdUser();
        }


    @Override
    public UtilisateurDto loginUser(LoginDto login) throws Exception {
            if(login == null || login.getEmail() == null || login.getPasswordHash() == null){
                throw new IllegalArgumentException("Les données de connexion sont invalides ! ");
            }
            Utilisateur user = utilisateurRepository.getUtilisateurByEmail(login.getEmail().trim());
            if(user == null){
                throw new IllegalArgumentException("Aucun utilisateur trouvé avec cette adresse email ! ");
            }
            if(!verifyPassword(login.getPasswordHash(), user.getPasswordHash())){
                throw new IllegalArgumentException("Mot de passe incorrect ! ");
            }
        return null;
    }
}
