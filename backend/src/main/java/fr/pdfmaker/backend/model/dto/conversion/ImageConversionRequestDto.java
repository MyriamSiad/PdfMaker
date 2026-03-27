package fr.pdfmaker.backend.model.dto.conversion;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.file.Path;

/**
 * DTO pour la conversion image → PDF.
 * Partagé par ImageToPdfService et PngToPdfService —
 * les deux formats ont exactement les mêmes besoins côté requête.
 *
 * La distinction JPEG / PNG est faite dans le service via verifierFormat().
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageConversionRequestDto extends ConversionRequestDto {

    @NotBlank(message = "Le nom du fichier de sortie est obligatoire")
    private String nomFichierSortie;

    private boolean adapterALaPage = true;


}
