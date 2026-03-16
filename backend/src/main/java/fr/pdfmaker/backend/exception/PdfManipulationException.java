package fr.pdfmaker.backend.exception;

/**
 * Levée quand une opération de manipulation de PDF échoue :
 * fusion, séparation ou annotation.
 * Encapsule les exceptions techniques de PDFBox.
 */
public class PdfManipulationException extends RuntimeException {

    public PdfManipulationException(String message) {
        super(message);
    }

    public PdfManipulationException(String message, Throwable cause) {
        super(message, cause);
    }
}