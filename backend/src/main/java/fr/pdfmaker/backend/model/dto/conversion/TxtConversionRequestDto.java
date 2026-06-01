package fr.pdfmaker.backend.model.dto.conversion;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class TxtConversionRequestDto extends ConversionRequestDto {
    @NotBlank(message = "Le nom du fichier de sortie est obligatoire")
    private String nomFichierSortie;



}
