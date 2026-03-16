package fr.pdfmaker.backend.exception;

/**
 * Levée quand une conversion de fichier vers PDF échoue
 * (TXT → PDF, JPEG → PDF, PNG → PDF).
 * Encapsule les exceptions techniques d'OpenPDF.
 */
public class ConversionException extends RuntimeException {

    public ConversionException(String message) {
        super(message);
    }

    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}