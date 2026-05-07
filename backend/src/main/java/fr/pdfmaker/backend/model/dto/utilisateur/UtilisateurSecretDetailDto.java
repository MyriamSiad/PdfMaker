package fr.pdfmaker.backend.model.dto.utilisateur;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UtilisateurSecretDetailDto {

    private Long idUser;
    private String passwordHash;
    private String salt;
    private String masterKey;

}
