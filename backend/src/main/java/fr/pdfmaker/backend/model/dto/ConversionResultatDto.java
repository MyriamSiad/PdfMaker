package fr.pdfmaker.backend.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO pour représenter le résultat d'une conversion de fichier.
 */
@Setter
@Getter

@Builder
public class ConversionResultatDto {


    private boolean success;
    private String outputPath;
    private String errorMessage;
}
