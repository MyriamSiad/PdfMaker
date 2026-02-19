package fr.pdfmaker.backend.model.dto;





import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class UtilisateurUpdateDto {

    private Long idUser;
    private String nom;
    private String email;
    private String prenom;
    private String passwordHash;

}
