package fr.pdfmaker.backend.model.dto.separation;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.file.Path;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SeparationRequestDto {

    @NotNull(message = "Le chemin du fichier PDF source est obligatoire")
    private Path cheminFichier;

    @NotEmpty(message = "La liste des pages à extraire ne peut pas être vide")
    private List<@Positive(message = "Les numéros de page doivent être positifs (base 1)") Integer> pages;

    @NotNull(message = "Le chemin de sortie est obligatoire")
    private Path cheminSortie;

    @NotBlank(message = "Le nom du fichier de sortie est obligatoire")
    private String nomFichierSortie;

}
