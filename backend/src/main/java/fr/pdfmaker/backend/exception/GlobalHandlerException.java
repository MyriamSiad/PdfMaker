package fr.pdfmaker.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Gestionnaire centralisé des exceptions.
 * Intercepte toutes les exceptions métier et retourne
 * une réponse JSON structurée au frontend JavaFX.
 */
@RestControllerAdvice
public class GlobalHandlerException {


    @ExceptionHandler(FichierIntrouvableException.class)
    public ResponseEntity<Map<String, Object>> handleFichierIntrouvable(
            FichierIntrouvableException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }


    @ExceptionHandler(UnsupportedFormatException.class)
    public ResponseEntity<Map<String, Object>> handleUnsupportedFormat(
            UnsupportedFormatException ex) {
        return buildResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage());
    }


    @ExceptionHandler(ConversionException.class)
    public ResponseEntity<Map<String, Object>> handleConversion(
            ConversionException ex) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }


    @ExceptionHandler(PdfManipulationException.class)
    public ResponseEntity<Map<String, Object>> handlePdfManipulation(
            PdfManipulationException ex) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenerique(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Erreur interne inattendue : " + ex.getMessage());
    }


    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status, String message) {

        Map<String, Object> corps = new LinkedHashMap<>();
        corps.put("timestamp", LocalDateTime.now().toString());
        corps.put("status", status.value());
        corps.put("erreur", status.getReasonPhrase());
        corps.put("message", message);

        return ResponseEntity.status(status).body(corps);
    }

    @ExceptionHandler(EmailDejaUtiliserException.class)
    public ResponseEntity<Map<String, String>> gererEmailDejaUtilise(EmailDejaUtiliserException e) {
        Map<String, String> erreur = new HashMap<>();
        erreur.put("message", e.getMessage());
        return ResponseEntity.status(409).body(erreur); // 409 = Conflict
    }

    @ExceptionHandler (LoginIncorrectException.class)
    public ResponseEntity<Map<String, String>> gererLoginIncorrect(LoginIncorrectException e) {
        Map<String, String> erreur = new HashMap<>();
        erreur.put("message", e.getMessage());
        return ResponseEntity.status(401).body(erreur); // 401 = Unauthorized
    }
}
