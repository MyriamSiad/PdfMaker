package fr.pdfmaker.backend.model.dto;

public class AnnotationDto {

    private int pageNumber;
    private float x; // coin inférieur gauche
    private float y;
    private float width;
    private float height;
    private String type; // "highlight" ou "text"
    private String content; // texte si type=text
    private String color;   // code hex ou RGB
}
