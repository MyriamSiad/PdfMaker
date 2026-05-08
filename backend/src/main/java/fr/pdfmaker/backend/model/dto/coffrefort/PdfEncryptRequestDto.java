package fr.pdfmaker.backend.model.dto.coffrefort;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PdfEncryptRequestDto {

    private String masterKey;

    private MultipartFile fichierPdf;

    private String titre;

    private String motDePasse;
   /* private String nomStockage;

    private String cheminLocal = "";*/

    private LocalDateTime dateAjout =  LocalDateTime.now();

}
