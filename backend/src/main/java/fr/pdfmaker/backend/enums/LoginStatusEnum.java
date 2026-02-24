package fr.pdfmaker.backend.enums;

import fr.pdfmaker.backend.mongo.document.LoginHistorique;
import lombok.Getter;
import lombok.extern.java.Log;


@Getter
public enum LoginStatusEnum {
    SUCCESS ("Réussite"),
    FAILURE ("Échec");

    private final String label;

    LoginStatusEnum (String label) {
        this.label = label;
    }
}
