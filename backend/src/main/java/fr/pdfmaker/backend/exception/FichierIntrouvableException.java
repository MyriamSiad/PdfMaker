package fr.pdfmaker.backend.exception;

import java.nio.file.Path;

public class FichierIntrouvableException extends  RuntimeException  {

    public FichierIntrouvableException(String message) {
        super(message);
    }

    public FichierIntrouvableException(Path chemin) {
        super("Fichier introuvable : " + chemin.toAbsolutePath());
    }

    public FichierIntrouvableException(Path chemin, Throwable cause) {
        super("Fichier introuvable : " + chemin.toAbsolutePath(), cause);
    }

    public FichierIntrouvableException(String message, Throwable cause) {
        super(message, cause);
    }
}