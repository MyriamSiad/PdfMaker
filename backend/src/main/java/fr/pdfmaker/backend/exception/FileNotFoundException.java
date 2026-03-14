package fr.pdfmaker.backend.exception;

public class FileNotFoundException extends ConversionException {
    public FileNotFoundException(String path) {
        super("Fichier introuvable : " + path);
    }
}
