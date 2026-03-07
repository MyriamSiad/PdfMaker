package fr.pdfmaker.backend.model.dto;


import fr.pdfmaker.backend.validation.annotation.ValidEmail;
import fr.pdfmaker.backend.validation.annotation.ValidName;
import fr.pdfmaker.backend.validation.annotation.ValidPassword;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class InscriptionRequestDto {

    private Long idUser;

    @ValidName
    private String nom;
    @ValidEmail
    private String email;
    @ValidName
    private String prenom;
    @ValidPassword
    private String passwordHash;
    private LocalDate dateCreationCompte;


}
