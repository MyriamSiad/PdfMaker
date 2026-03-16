package fr.pdfmaker.backend.model.dto.fusion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.file.Path;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FusionRequestDto {

    @NotEmpty(message = "La liste des fichiers à fusionner ne peut pas être vide")
    @Size(min = 2, message = "La fusion nécessite au minimum 2 fichiers PDF")
    private List<Path> cheminsFichiers;

    @NotNull(message = "Le chemin de sortie est obligatoire")
    private Path cheminSortie;

    @NotBlank(message = "Le nom du fichier de sortie est obligatoire")
    private String nomFichierSortie;


}
