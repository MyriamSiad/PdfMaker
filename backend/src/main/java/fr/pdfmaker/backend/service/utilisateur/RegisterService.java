package fr.pdfmaker.backend.service.utilisateur;

import fr.pdfmaker.backend.model.dto.utilisateur.InscriptionRequestDto;
import fr.pdfmaker.backend.model.dto.utilisateur.UtilisateurDto;
import fr.pdfmaker.backend.model.entity.Utilisateur;
import fr.pdfmaker.backend.repository.IUtilisateurRepository;
import fr.pdfmaker.backend.security.BCryptPasswordService;
import fr.pdfmaker.backend.security.IPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HexFormat;

import static fr.pdfmaker.backend.utils.DtoUserConverter.convertUserDtoToUser;

@Service
@RequiredArgsConstructor

/**
 * C'est une classe de service qui se charge de l'inscription d'un utilisateur,
 * elle utilise le repository pour enregistrer les données de l'utilisateur dans la base de données, et le service de mot de passe pour hasher le mot de passe avant de l'enregistrer.
 */
public class RegisterService {
        private final IPasswordService passwordService;
        private final IUtilisateurRepository iUtilisateurRepository;


        public String hashGenrator (){
            SecureRandom random = new SecureRandom();
            byte[] saltBytes = new byte[16];
            random.nextBytes(saltBytes);
            return HexFormat.of().formatHex(saltBytes);
        }
    /**
     * Cette méthode prend un DTO d'inscription, hash le mot de passe et enregistre l'utilisateur dans la base de données, elle retourne un DTO de l'utilisateur enregistré.
     * @param inscriptionRequestDto
     * @return
     * @throws Exception
     */
        public UtilisateurDto registerUser(InscriptionRequestDto inscriptionRequestDto) throws Exception {
           passwordService.hash(inscriptionRequestDto.getPasswordHash());
           iUtilisateurRepository.save(convertUserDtoToUser(inscriptionRequestDto));

          return new UtilisateurDto();
        }



}
