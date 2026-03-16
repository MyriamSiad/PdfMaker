package fr.pdfmaker.backend.exception;

/**
 * Exception personnalisée pour indiquer qu'un format de fichier n'est pas pris en charge.
 * Cette exception peut être levée lorsque le format d'entrée ou de sortie n'est pas valide pour la conversion en PDF.
 */
public class UnsupportedFormatException extends RuntimeException{
    public UnsupportedFormatException(String message) {
        super(message);
    }

    public UnsupportedFormatException(String formatAttendu, String formatDetecte) {
        super("Format invalide — attendu : " + formatAttendu + ", détecté : " + formatDetecte);
    }

    public UnsupportedFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
