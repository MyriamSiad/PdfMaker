package fr.pdfmaker.backend.exception;

public class UnsupportedFormatException extends ConversionException {

    public UnsupportedFormatException(String format) {
        super("Format non supporté : " + format);
    }
}
