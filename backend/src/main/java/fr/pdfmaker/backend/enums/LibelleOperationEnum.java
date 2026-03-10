package fr.pdfmaker.backend.enums;

import lombok.Getter;

@Getter
public enum LibelleOperationEnum {
    CONVERSION("Conversion"),
    FUSION("Fusion"),
    SEPARATION("Separation"),
    ANNOTATION("Annotation");

    private final String libelleOperation;

    LibelleOperationEnum(String libelleOperation) {
        this.libelleOperation = libelleOperation;
    }

}
