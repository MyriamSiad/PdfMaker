package fr.pdfmaker.backend.model.dto.fichier;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter

public class FichierDto {

    private Long idFichier;

    private String nomOriginal;

    private LocalDateTime dateAjout;

    private String nomStockage;

    private String cheminLocal;

}
