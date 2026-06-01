package fr.pdfmaker.backend.model.dto.conversion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
/**
 * Classe abstraite parente de tous les DTOs de conversion.
 * Porte les deux champs communs à toute opération :
 *   - cheminFichier : fichier source à convertir
 *   - charset : encodage du fichier source (par défaut UTF-8)
 * Cette classe est conçue pour être étendue par des DTOs spécifiques à chaque type de conversion
 * afin de réutiliser les champs et la validation commune.
 */

@Getter
@Setter
@NoArgsConstructor
public class ConversionRequestDto {

    private String charset = "UTF-8";
    @NotNull(message = "Le fichier source est obligatoire")
    private MultipartFile fichier;

    @NotBlank(message = "Le nom du fichier de sortie est obligatoire")
    private String nomFichierSortie;





}
