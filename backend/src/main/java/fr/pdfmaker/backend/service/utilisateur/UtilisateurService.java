package fr.pdfmaker.backend.service.utilisateur;

import fr.pdfmaker.backend.exception.EmailDejaUtiliserException;
import fr.pdfmaker.backend.exception.LoginIncorrectException;
import fr.pdfmaker.backend.model.dto.utilisateur.InscriptionRequestDto;
import fr.pdfmaker.backend.model.dto.utilisateur.LoginDto;
import fr.pdfmaker.backend.model.dto.utilisateur.UtilisateurDto;
import fr.pdfmaker.backend.model.entity.Utilisateur;
import fr.pdfmaker.backend.repository.IUtilisateurRepository;
import fr.pdfmaker.backend.security.BruteForceProtectionService;
import fr.pdfmaker.backend.service.coffrefort.EncryptService;
import fr.pdfmaker.backend.service.dossier.DossierStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountLockedException;

import static fr.pdfmaker.backend.utils.DtoUserConverter.*;

@Service
public class UtilisateurService implements IUtilisateurService {



    @Autowired
    private IUtilisateurRepository utilisateurRepository;

    @Autowired
    private EncryptService encryptService;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private DossierStorageService dossierStorageService;

    @Autowired
    private BruteForceProtectionService bruteForce;

    @Override
    public UtilisateurDto getUtilsateur(long id) throws Exception {

        Utilisateur utilisateur = utilisateurRepository.findById(id).get();

        return convertUserToUserDto(utilisateur);
    }

    /**
     * Methode qui permet de verfier si l'objet donné en paramètre est bien valide.
     * S'il contient bien toutes les infos de l'utilisateur pour la création de son compte
     * @param user c'est l'utilisateur (objet) qu'on veut vérifier,
     * @author Myriam S.
     */
    private void verifyUserInfo(InscriptionRequestDto user){

        if (user == null ||
                user.getNom() == null ||
                user.getEmail()  == null ||
                user.getPrenom() == null ||
                user.getPasswordHash() == null) {
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
            throw new EmailDejaUtiliserException("Cet email est déjà utilisé");
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
    public boolean verifyPassword(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }



    @Override
    @Transactional
    public Long createUser( InscriptionRequestDto user) throws Exception {
            verifyExistingUserEmail(user.getEmail());
            verifyUserInfo(user);


            user.setSalt(encryptService.saltGenerator());
            String masterKey = encryptService.chiffrageMasterKey(encryptService.masterKeyGenerator(), encryptService.secretKeyGenerator(user.getPasswordHash(), user.getSalt()));
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
            user.setMasterKey(masterKey);
            Utilisateur _user = convertUserDtoToUser(user);
            Long id =  utilisateurRepository.save(_user).getIdUser();
            dossierStorageService.initialiserDossier(id, "Mon Coffre-" + user.getPrenom());

            return id;

    }

    @Override
    @Transactional
    public Long updateUser (UtilisateurDto user) throws Exception {

            if( !utilisateurRepository.existsById(user.getIdUser())){
                throw new IllegalArgumentException("Cet utilisateur n'existe pas !  ");
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
            if( login.getEmail() == null || login.getMotsDePasse() == null){

                throw new IllegalArgumentException("Les données de connexion sont invalides ! ");

            }

            Utilisateur user = utilisateurRepository.getUtilisateurByEmail(login.getEmail().trim());
            if(user == null){
                throw new LoginIncorrectException("Login ou mot de passe incorrect. Veuillez réessayer.");
            }
            if (bruteForce.isLocked(user.getLockedUntil())) {
                throw new AccountLockedException("Compte verrouillé temporairement");
            }
            if(!verifyPassword(login.getMotsDePasse(), user.getPasswordHash())){
                user.setFailedAttempts(user.getFailedAttempts() + 1);
                if(bruteForce.locking(user.getFailedAttempts())){
                    user.setLockedUntil(bruteForce.lockUntil(user.getFailedAttempts()));
                }
                utilisateurRepository.save(user);
                throw new LoginIncorrectException("Mots de passe incorrect ! ");
            }


        user.setFailedAttempts(0);
        user.setLockedUntil(null);
        utilisateurRepository.save(user);
        return convertUserToUserDto(user);
    }
}
