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
    private String message;
    private byte[] fichierPdf;
    private String nomFichierSortie;

    public ConversionResultatDto(String nomFichierSortie, byte[] fichierPdf) {
        this.success = true;
        this.fichierPdf = fichierPdf;
        this.message = "Conversion réussie";
        this.nomFichierSortie = nomFichierSortie;
    }

}
