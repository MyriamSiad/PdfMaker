package fr.pdfmaker.backend.enums;

public enum LibelleOperationEnum {
    CONVERSION("Conversion"),
    FUSION("Fusion"),
    SEPARATION("Separation"),
    ANNOTATION("Annotation");

    private String libelleOperation;

    LibelleOperationEnum(String libelleOperation) {
        this.libelleOperation = libelleOperation;
    }

    public String getLibelleOperation() {
        return libelleOperation;
    }
}
