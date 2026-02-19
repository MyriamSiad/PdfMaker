package fr.pdfmaker.backend.model.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

    private String email;
    private String passwordHash;
}
