package fr.pdfmaker.backend.model.dto.utilisateur;





import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
