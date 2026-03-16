package fr.pdfmaker.backend.model.dto.conversion;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.file.Path;
/**
 * Classe abstraite parente de tous les DTOs de conversion.
 * Porte les deux champs communs à toute opération :
 *   - cheminFichier : fichier source à convertir
 *   - cheminSortie  : répertoire de destination du PDF produit
 * Cette classe est conçue pour être étendue par des DTOs spécifiques à chaque type de conversion
 * afin de réutiliser les champs et la validation commune.
 */

@Getter
@Setter
@NoArgsConstructor
public class ConversionRequestDto {
    @NotNull(message = "Le chemin du fichier source est obligatoire")
    private Path cheminFichier;

    @NotNull(message = "Le chemin de sortie est obligatoire")
    private Path cheminSortie;

    private String charset = "UTF-8";


}
