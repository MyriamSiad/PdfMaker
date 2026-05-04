package fr.pdfmaker.backend.model.dto.coffrefort;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PdfEncryptRequestDto {

    private String masterKey;

    private byte[] fichierPdf;

    private String titre;

   /* private String nomStockage;

    private String cheminLocal = "";*/

    private LocalDateTime dateAjout =  LocalDateTime.now();

}
