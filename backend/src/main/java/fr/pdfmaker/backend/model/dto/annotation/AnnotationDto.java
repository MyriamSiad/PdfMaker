package fr.pdfmaker.backend.model.dto.annotation;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.file.Path;

@Getter
@Setter
@NoArgsConstructor
public class AnnotationDto {

    @NotNull(message = "Le chemin du fichier PDF source est obligatoire")
    private Path cheminFichier;

    @NotNull(message = "Le chemin de sortie est obligatoire")
    private Path cheminSortie;

    private int pageNumber;
    private float x; // coin inférieur gauche
    private float y;
    private float width;
    private float height;
    private String type; // "highlight" ou "text"
    private String content; // texte si type=text
    private String color;   // code hex ou RGB
}
