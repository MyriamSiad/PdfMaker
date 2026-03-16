package fr.pdfmaker.backend.model.dto.conversion;

import lombok.*;

/**
 * DTO pour représenter le résultat d'une conversion de fichier.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversionResultatDto {
    private boolean success;
    private byte[] fichierPdf;
    private String outputPath;
    private String message;

    public ConversionResultatDto(String outputPath, byte[] fichierPdf) {
        this.success = true;
        this.outputPath = outputPath;
        this.fichierPdf = fichierPdf;
        this.message = "Conversion réussie";
    }

}
