package fr.pdfmaker.backend.model.dto.utilisateur;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

    private String email;
    private String motsDePasse;


}
