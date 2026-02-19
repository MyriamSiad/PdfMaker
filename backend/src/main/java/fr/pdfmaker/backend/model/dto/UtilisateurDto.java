package fr.pdfmaker.backend.model.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class UtilisateurDto {

    private Long idUser;
    private String nom;
    private String prenom;
    private String email;
    private LocalDate dateCreationCompte;

}
